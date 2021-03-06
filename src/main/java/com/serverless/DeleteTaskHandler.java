package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DeleteTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(DeleteTaskHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> headersCollections = Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless");

        try {
            // get the 'pathParameters' from input
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String taskId = pathParameters.get("id");

            // get the Product by id
            Boolean success = new Task().delete(taskId);

            // send the response back
            if (success) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(204)
                        .setHeaders(headersCollections)
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Product with id: '" + taskId + "' not found.")
                        .setHeaders(headersCollections)
                        .build();
            }
        } catch (Exception ex) {
            LOG.error("Error in deleting product: %s ", ex);

            // send the error response back
            Response responseBody = new Response("Error in deleting product: ", input);
            try {
                return ApiGatewayResponse.builder()
                        .setStatusCode(500)
                        .setObjectBody(responseBody)
                        .setHeaders(headersCollections)
                        .build();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
