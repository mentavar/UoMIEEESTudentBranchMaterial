package io.grpc.java.testing_service.jmeter;

import io.grpc.StatusRuntimeException;
import io.grpc.java.testing_service.TestingServiceClient;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class TestingServiceSampler extends AbstractJavaSamplerClient {

    TestingServiceClient bclient = null;
    String username = "username";
    String password = "password";

    @Override
    public void setupTest(JavaSamplerContext context) {
        String environment = context.getParameter("host");
        String port = context.getParameter("port");
        this.username = context.getParameter("username");
        this.password = context.getParameter("password");
        this.bclient = new TestingServiceClient(environment, Integer.parseInt(port));
        super.setupTest(context);
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument("host", "localhost");
        defaultParameters.addArgument("port", "9091");
        defaultParameters.addArgument("username", username);
        defaultParameters.addArgument("password", password);
        return defaultParameters;
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        boolean success = true;
        String response = "";
        result.sampleStart();

        try {
            response = this.bclient.sendRequestToService(bclient, username, password).toString();
            result.sampleEnd();
            result.setSuccessful(success);
            result.setResponseData(response.getBytes());
            result.setResponseMessage("Successfully performed backup healthcheck");
            result.setResponseCodeOK(); // 200 code
        } catch (StatusRuntimeException e) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);
            success = false;
            result.setSuccessful(success);
            // get stack trace as a String to return as document data
            java.io.StringWriter stringWriter = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(stringWriter));
            result.setResponseData(stringWriter.toString().getBytes());
            result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
            result.setResponseCode("500");
        }
        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        try {
            bclient.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.teardownTest(context);
    }
}