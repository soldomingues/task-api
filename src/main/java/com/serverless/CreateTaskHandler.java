package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class CreateTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

        private static final Logger LOG = LogManager.getLogger(Handler.class);

        @Override
        public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

                try {
                        // get the 'body' from input
                        JsonNode body = new ObjectMapper().readTree((String) input.get("body"));


                        // create the Task object for post
                        Task task = new Task();
                        // product.setId(body.get("id").asText());
                        task.setTitle(body.get("title").asText());
                        task.save(task);

                        // send the response back
                        return ApiGatewayResponse.builder()
                                .setStatusCode(200)
                                .setObjectBody(task)
                                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                                .build();

                } catch (Exception ex) {
                        LOG.error("Error in saving task: " + ex);

                        // send the error response back
                        Response responseBody = new Response("Error in saving product: ", input);
                        return ApiGatewayResponse.builder()
                                .setStatusCode(500)
                                .setObjectBody(responseBody)
                                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                                .build();
                }
    }
}
