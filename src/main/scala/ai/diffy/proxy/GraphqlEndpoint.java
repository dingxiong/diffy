package ai.diffy.proxy;



public class GraphqlEndpoint {
  private final String operationName;
  private final GraphqlMethod method;

  public GraphqlEndpoint(final String operationName, final GraphqlMethod method) {
    this.operationName = operationName;
    this.method = method;
  }

  public String getOperationName() {
    return operationName;
  }

  public GraphqlMethod getMethod() {
    return method;
  }
}
