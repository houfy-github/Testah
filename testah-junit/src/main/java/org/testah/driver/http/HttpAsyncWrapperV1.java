package org.testah.driver.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;

import java.io.Closeable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The Class HttpAsyncWrapperV1.
 */
public class HttpAsyncWrapperV1 extends AbstractHttpWrapper implements Closeable {

    /**
     * The http async client.
     */
    private CloseableHttpAsyncClient httpAsyncClient;

    /**
     * Do request async.
     *
     * @param request the request
     * @param verbose the verbose
     * @return the future
     */
    public Future<HttpResponse> doRequestAsync(final AbstractRequestDto<?> request, final boolean verbose) {
        try {
            final HttpClientContext context = HttpClientContext.create();
            if (null != getCookieStore()) {
                context.setCookieStore(getCookieStore());
                context.setRequestConfig(getRequestConfig());
            }

            if (null != request.getCredentialsProvider()) {
                context.setCredentialsProvider(request.getCredentialsProvider());
            }

            new ResponseDto().setStart();
            if (verbose) {
                TS.step().action().add(request.createRequestInfoStep());
            }
            try {
                getHttpAsyncClient().start();
                final Future<HttpResponse> future = getHttpAsyncClient().execute(request.getHttpRequestBase(), context,
                    new FutureCallback<HttpResponse>() {

                        public void completed(final HttpResponse response) {
                            if (verbose) {
                                final ResponseDto responseDto = getResponseDto(response, request);
                                TS.step().action().add(responseDto.createResponseInfoStep(
                                    request.isTruncateResponseBodyInReport(), true,
                                    request.getTruncateResponseBodyInReportBy()));
                            }
                        }

                        public void failed(final Exception ex) {
                            if (verbose) {
                                TS.step().action().createInfo(
                                    "ERROR - Issue with request " + request.getHttpRequestBase().getRequestLine(),
                                    ex.getMessage());
                            }
                        }

                        public void cancelled() {
                            if (verbose) {
                                TS.step().action().createInfo("Canceled Request",
                                    request.getHttpRequestBase().getRequestLine().toString());
                            }
                        }
                    });
                return future;
            } finally {
                // need to complete try block, maybe move declaration of Future<HttpResponse> future = null out of try block
                // and return future here.
            }
        } catch (final Exception e) {
            TS.log().error(e);
            if (!isIgnoreHttpError()) {
                TS.asserts().equalsTo("Unexpected Exception thrown from preformRequest in IHttpWrapper", "",
                    e.getMessage());
            }
            return null;
        }
    }

    /**
     * Gets the http async client.
     *
     * @return the http async client
     */
    public CloseableHttpAsyncClient getHttpAsyncClient() {
        if (null == httpAsyncClient) {
            setHttpAsyncClient();
        }
        return httpAsyncClient;
    }

    /**
     * Sets the http async client.
     *
     * @param httpAsyncClient the http async client
     * @return the http async wrapper v1
     */
    public HttpAsyncWrapperV1 setHttpAsyncClient(final CloseableHttpAsyncClient httpAsyncClient) {
        this.httpAsyncClient = httpAsyncClient;
        return this;
    }

    /**
     * Sets http async client.
     *
     * @return the http async client
     */
    public AbstractHttpWrapper setHttpAsyncClient() {
        return setHttpAsyncClient(getHttpAsyncClientBuilder().build());
    }

    /**
     * Sets the http async client.
     *
     * @return the abstract http wrapper
     */
    public HttpAsyncClientBuilder getHttpAsyncClientBuilder() {
        final HttpAsyncClientBuilder hcb = HttpAsyncClients.custom();
        if (null != getProxy()) {
            hcb.setProxy(getProxy());
        }
        if (null != getRequestConfig()) {
            hcb.setDefaultRequestConfig(getRequestConfig());
        }
        if (null != getCookieStore()) {
            hcb.setDefaultCookieStore(getCookieStore());
        }
        try {
            if (null != getConnectionManager()) {
                final ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
                final PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(
                    ioReactor);
                connManager.setMaxTotal(100);
                hcb.setConnectionManager(connManager);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        hcb.setMaxConnPerRoute(getDefaultMaxPerRoute());
        hcb.setMaxConnTotal(getDefaultPoolSize());
        if (isTrustAllCerts()) {
            // hcb.setSSLHostnameVerifier(new NoopHostnameVerifier());
        }
        return hcb;
    }

    /**
     * Wait for future response http response.
     *
     * @param response               the response
     * @param maxTimeToWaitInSeconds the max time to wait in seconds
     * @return the http response
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public HttpResponse waitForFutureResponse(final Future<HttpResponse> response, final int maxTimeToWaitInSeconds)
        throws ExecutionException, InterruptedException {
        int ctr = 1;
        while (!response.isDone() || response.isCancelled()) {
            TS.util().pause(200L, "Waiting for response future to be done", ctr++);
            if (ctr > maxTimeToWaitInSeconds) {
                break;
            }
        }
        getVerboseAsserts().isTrue("Check that the future is done after waiting for it", response.isDone());
        return response.get();
    }

    /**
     * Gets the response dto from future.
     *
     * @param response the response
     * @return the response dto from future
     * @throws Exception the exception
     */
    public ResponseDto getResponseDtoFromFuture(final Future<HttpResponse> response) throws Exception {
        return getResponseDtoFromFuture(response, null);
    }

    /**
     * Gets response dto from future.
     *
     * @param response the response
     * @param request  the request
     * @return the response dto from future
     * @throws Exception the exception
     */
    public ResponseDto getResponseDtoFromFuture(final Future<HttpResponse> response,
                                                final AbstractRequestDto<?> request) throws Exception {
        return getResponseDtoFromFuture(response, request, 120);
    }

    /**
     * Gets the response dto from future.
     *
     * @param response               the response
     * @param request                the request
     * @param maxTimeToWaitInSeconds the max time to wait in seconds
     * @return the response dto from future
     * @throws Exception the exception
     */
    public ResponseDto getResponseDtoFromFuture(final Future<HttpResponse> response,
                                                final AbstractRequestDto<?> request, final int maxTimeToWaitInSeconds)
        throws Exception {
        if (null != response) {
            TS.log().debug("Getting response from future, current done state is: " + response.isDone() +
                " will block until done if needed.");
            final HttpResponse responseFromFuture = waitForFutureResponse(response, maxTimeToWaitInSeconds);
            return getResponseDto(responseFromFuture, request);
        }
        return null;
    }

    /**
     * Close the async HTTP client.
     *
     * @see java.io.Closeable#close()
     */
    public void close() {
        try {
            getHttpAsyncClient().close();
        } catch (final Exception e) {
            TS.log().warn(e);
        }
    }

    protected AbstractHttpWrapper getSelf() {
        return this;
    }

}
