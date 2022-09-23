package ai.diffy.proxy;

import java.util.Map;
import io.netty.handler.codec.http.HttpMethod;
import java.util.HashMap;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class HttpRequest {
    private final HttpMethod method;
    private final String uri;
    private final String path;
    private final Map<String, String> params;
    private final HttpMessage message;
    private final Optional<GraphqlEndpoint> graphqlData;
    
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    static private final ObjectMapper MAPPER;
    static private final TypeReference<HashMap<String,Object>> MAP_TYPE_REF;


    static {
        MAPPER = new ObjectMapper();
        MAP_TYPE_REF = new TypeReference<HashMap<String,Object>>() {};
    }

    public HttpRequest(HttpMethod method, String uri, String path, Map<String, String> params, HttpMessage message) {
        this.method = method;
        this.uri = uri;
        this.path = path;
        this.params = params;
        this.message = message;
        this.graphqlData = buildGraphqlData();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public HttpMessage getMessage() {
        return message;
    }
    
    public Optional<GraphqlEndpoint> getGraphqlData() {
        return graphqlData;
    }

    private Optional<GraphqlEndpoint> buildGraphqlData() {
        if (!this.path.equals("graphql")) 
            return Optional.empty();
        try {
           Map<String, Object> body = MAPPER.readValue(message.getBody(), MAP_TYPE_REF);
           String operationName = body.get("operationName").toString();
           GraphqlMethod method = body.get("query").toString().startsWith("query") ? GraphqlMethod.Query : GraphqlMethod.Mutation;
           return Optional.of(new GraphqlEndpoint(operationName, method));
        } catch (Exception e) {
           log.error("Fail to decode graphql request {}. Error: {}", message.getBody(), e);
        }
        return Optional.empty();
    }

    

    @Override
    public String toString() {
        return "path = "+ path+"\n"+"params =\n"+ params+"\n"+"message =\n"+ message+"\n";
    }
}
