package com.kttdevelopment.myanimelist;

import com.kttdevelopment.myanimelist.APIStruct.ENDPOINT;
import com.kttdevelopment.myanimelist.APIStruct.Response;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kttdevelopment.myanimelist.APIStruct.*;

final class APICall {

    private final String method;
    private final String baseURL;
    private final String path;

    APICall(final String method, final String baseURL, final String path){
        this.method     = method;
        this.baseURL    = baseURL;
        this.path       = path;
    }

    APICall(final String baseURL, final Method method, final Object[] args){
        this.baseURL = baseURL;

        final ENDPOINT endpoint = method.getAnnotation(ENDPOINT.class);
        if(endpoint != null){
            this.method = endpoint.method();
            path = endpoint.value();
        }else{
            this.method = "GET";
            this.path = "";
        }

        if(method.getAnnotation(FormUrlEncoded.class) != null)
            formUrlEncoded = true;

        for(int i = 0, size = method.getParameterAnnotations().length; i < size; i++){
            final Object arg = args[i];
            for(final Annotation annotation : method.getParameterAnnotations()[i]){
                final Class<? extends Annotation> type = annotation.annotationType();
                if(type == Path.class)
                    withPathVar(((Path) annotation).value(), arg, ((Path) annotation).encoded());
                else if(type == Header.class)
                    withHeader(((Header) annotation).value(), Objects.toString(arg));
                else if(type == Query.class)
                    withQuery(((Query) annotation).value(), arg, ((Query) annotation).encoded());
                else if(type == Field.class)
                    withField(((Query) annotation).value(), arg, ((Query) annotation).encoded());
            }
        }
    }

    private final Map<String,String> headers = new HashMap<>();

    // \{(.*?)\}
    private static final Pattern pathArg = Pattern.compile("\\{(.*?)}");

    private final Map<String,String> pathVars = new HashMap<>();
    private final Map<String,String> queries  = new HashMap<>();

    private boolean formUrlEncoded = false;
    private final Map<String,String> fields = new HashMap<>();

    final APICall withHeader(final String header, final String value){
        if(value == null)
            headers.remove(header);
        else
            headers.put(header, value);
        return this;
    }

    final APICall withPathVar(final String pathVar, final String value){
        return withPathVar(pathVar, value, false);
    }

    final APICall withPathVar(final String pathVar, final Object value, final boolean encoded){
        if(value == null)
            pathVars.remove(pathVar);
        else
            pathVars.put(pathVar, encoded ? Objects.toString(value) : URLEncoder.encode(Objects.toString(value), StandardCharsets.UTF_8));
        return this;
    }

    final APICall withQuery(final String query, final Object value){
        return withQuery(query, value, false);
    }

    final APICall withQuery(final String query, final Object value, final boolean encoded){
        if(value == null)
            queries.remove(query);
        else
            queries.put(query, encoded ? Objects.toString(value) : URLEncoder.encode(Objects.toString(value), StandardCharsets.UTF_8));
        return this;
    }

    final APICall formUrlEncoded(){
        return formUrlEncoded(true);
    }

    final APICall formUrlEncoded(final boolean formUrlEncoded){
        this.formUrlEncoded = formUrlEncoded;
        return this;
    }

    final APICall withField(final String field, final Object value){
        return withField(field, value, false);
    }

    final APICall withField(final String field, final Object value, final boolean encoded){
        if(value == null)
            fields.remove(field);
        else
            fields.put(field, encoded ? Objects.toString(value) : URLEncoder.encode(Objects.toString(value), StandardCharsets.UTF_8));
        return this;
    }

    final Response<String> call() throws IOException{
        final String URL =
            baseURL +
            pathArg.matcher(path).replaceAll(result -> pathVars.get(result.group())) + // path args
            (queries.isEmpty() ? "" : queries.entrySet().stream().map(e -> e.getKey() + '=' +e.getValue()).collect( Collectors.joining("&"))); // query

        final HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
        conn.setRequestMethod(method);
        for(final Map.Entry<String, String> entry : headers.entrySet())
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(false);

        if(formUrlEncoded){
            final String data = fields.isEmpty() ? "" : fields.entrySet().stream().map(e -> e.getKey() + '=' +e.getValue()).collect( Collectors.joining("&"));
            final byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));

            try(final DataOutputStream OUT = new DataOutputStream(conn.getOutputStream())){
                OUT.write(bytes);
            }
        }

        final int rcode = conn.getResponseCode();

        return new Response<>(
            new BufferedReader(new InputStreamReader(rcode >= 200 && rcode < 300 ? conn.getInputStream() : conn.getErrorStream()))
                .lines()
                .collect(Collectors.joining("\n")),
            rcode
        );
    }

    final <T> Response<T> call(final Function<String,T> processor) throws IOException{
        final Response<String> call = call();
        return new Response<T>(processor.apply(call.response()), call.code());
    }

    @Override
    public String toString(){
        return "APICall{" +
               "method='" + method + '\'' +
               ", baseURL='" + baseURL + '\'' +
               ", headers=" + headers +
               ", path='" + path + '\'' +
               ", pathVars=" + pathVars +
               ", queries=" + queries +
               ", formUrlEncoded=" + formUrlEncoded +
               ", fields=" + fields +
               '}';
    }

}
