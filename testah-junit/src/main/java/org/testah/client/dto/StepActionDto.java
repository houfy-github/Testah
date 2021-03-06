package org.testah.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.testah.TS;
import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestStepActionType;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class StepActionDto.
 */
public class StepActionDto extends AbstractDtoBase<StepActionDto> {

    /**
     * The test step action type.
     */
    private TestStepActionType testStepActionType = TestStepActionType.INFO;

    /**
     * The description.
     */
    private String description = null;

    /**
     * The message1.
     */
    private String message1 = null;

    /**
     * The message2.
     */
    private String message2 = null;

    /**
     * The message3.
     */
    private String message3 = null;

    /**
     * The status.
     */
    private Boolean status = null;

    /**
     * The status enum.
     */
    private TestStatus statusEnum = null;

    /**
     * The action name.
     */
    private String actionName = null;

    /**
     * The exception.
     */
    @JsonIgnore
    private Throwable exception = new Throwable();

    /**
     * The exception string.
     */
    private String exceptionString = null;

    /**
     * The snap shot path.
     */
    private String snapShotPath = null;

    /**
     * The snap shot path.
     */
    private String htmlSnapShotPath = null;

    private String restResponsePath = null;

    /**
     * Instantiates a new step action dto.
     */
    public StepActionDto() {

    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        if (null == this.description) {
            this.description = getActionName() + "[" + message1 + "]";
        }
        return this.description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     * @return the step action dto
     */
    public StepActionDto setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the action name.
     *
     * @return the action name
     */
    public String getActionName() {
        if (null == actionName) {
            actionName = getTestStepActionType().name();
        }
        return actionName;
    }

    /**
     * Sets the action name.
     *
     * @param actionName the action name
     * @return the step action dto
     */
    public StepActionDto setActionName(final String actionName) {
        this.actionName = actionName;
        return this;
    }

    /**
     * Gets the test step action type.
     *
     * @return the test step action type
     */
    public TestStepActionType getTestStepActionType() {
        return testStepActionType;
    }

    /**
     * Sets the test step action type.
     *
     * @param testStepActionType the test step action type
     * @return the step action dto
     */
    public StepActionDto setTestStepActionType(final TestStepActionType testStepActionType) {
        this.testStepActionType = testStepActionType;
        return this;
    }

    /**
     * Gets the assert method.
     *
     * @return the assert method
     */
    public String getAssertMethod() {
        return actionName;
    }

    /**
     * Sets the assert method.
     *
     * @param assertMethod the assert method
     * @return the step action dto
     */
    public StepActionDto setAssertMethod(final String assertMethod) {
        this.actionName = assertMethod;
        return this;
    }

    /**
     * Gets the actual.
     *
     * @return the actual
     */
    public Object getActual() {
        return getMessage3();
    }

    /**
     * Sets the actual.
     *
     * @param actual the actual
     * @return the step action dto
     */
    public StepActionDto setActual(final String actual) {
        return setMessage3(actual);
    }

    /**
     * Gets the message3.
     *
     * @return the message3
     */
    public Object getMessage3() {
        return message3;
    }

    /**
     * Sets the message3.
     *
     * @param message3 the message3
     * @return the step action dto
     */
    public StepActionDto setMessage3(final String message3) {
        this.message3 = message3;
        return this;
    }

    /**
     * Gets the expected.
     *
     * @return the expected
     */
    public Object getExpected() {
        return getMessage2();
    }

    /**
     * Sets the expected.
     *
     * @param expected the expected
     * @return the step action dto
     */
    public StepActionDto setExpected(final String expected) {
        return setMessage2(expected);
    }

    /**
     * Gets the message2.
     *
     * @return the message2
     */
    public Object getMessage2() {
        return message2;
    }

    /**
     * Sets the message2.
     *
     * @param message2 the message2
     * @return the step action dto
     */
    public StepActionDto setMessage2(final String message2) {
        this.message2 = message2;
        return this;
    }

    /**
     * Gets the exception.
     *
     * @return the exception
     */
    @JsonIgnore
    public Throwable getException() {
        return exception;
    }

    /**
     * Sets the exception.
     *
     * @param exception the exception
     * @return the step action dto
     */
    public StepActionDto setException(final Throwable exception) {
        this.exception = exception;
        return this;
    }

    /**
     * Gets exception string.
     *
     * @return the exception string
     */
    public String getExceptionString() {
        return getExceptionString(true);
    }

    /**
     * Gets the exception string.
     *
     * @param isReturningJsonObject Used when the testplan object will be a json object
     * @return the exception string
     */
    public String getExceptionString(boolean isReturningJsonObject) {
        if (null == exception) {
            return null;
        } else if (isReturningJsonObject && (status == null || status.equals(Boolean.TRUE))) {
            return null;
        } else {
            if (null == exceptionString) {
                final StringWriter sWriter = new StringWriter();
                exception.printStackTrace(new PrintWriter(sWriter));
                exceptionString = sWriter.toString().replace("\t", "");
            }
        }

        return exceptionString;
    }

    /**
     * Sets the exception string.
     *
     * @param exceptionString the exception string
     * @return the step action dto
     */
    public StepActionDto setExceptionString(final String exceptionString) {
        this.exceptionString = exceptionString;
        return this;
    }

    /**
     * Sets the message.
     *
     * @param message the message
     * @return the step action dto
     */
    public StepActionDto setMessage(final String message) {
        this.message1 = message;
        return this;
    }

    /**
     * Gets the snap shot path.
     *
     * @return the snap shot path
     */
    public String getSnapShotPath() {
        return snapShotPath;
    }

    /**
     * Sets the snap shot path.
     *
     * @param snapShotPath the snap shot path
     * @return the step action dto
     */
    public StepActionDto setSnapShotPath(final String snapShotPath) {
        this.snapShotPath = snapShotPath;
        return this;
    }

    /**
     * Gets the status enum.
     *
     * @return the status enum
     */
    public TestStatus getStatusEnum() {
        if (null == statusEnum) {
            this.statusEnum = TestStatus.getStatus(status);
        }
        return this.statusEnum;
    }

    /**
     * Sets the status enum.
     *
     * @param statusEnum the status enum
     * @return the step action dto
     */
    public StepActionDto setStatusEnum(final TestStatus statusEnum) {
        this.statusEnum = statusEnum;
        return this;
    }

    /**
     * Gets the html snap shot path.
     *
     * @return the html snap shot path
     */
    public String getHtmlSnapShotPath() {
        return htmlSnapShotPath;
    }

    /**
     * Sets the html snap shot path.
     *
     * @param htmlSnapShotPath the html snap shot path
     * @return the step action dto
     */
    public StepActionDto setHtmlSnapShotPath(final String htmlSnapShotPath) {
        this.htmlSnapShotPath = htmlSnapShotPath;
        return this;
    }

    /**
     * Gets rest response path.
     *
     * @return the rest response path
     */
    public String getRestResponsePath() {
        return restResponsePath;
    }

    /**
     * Sets rest response path.
     *
     * @param restResponsePath the rest response path
     */
    public void setRestResponsePath(String restResponsePath) {
        this.restResponsePath = restResponsePath;
    }

    /**
     * Log step action dto.
     *
     * @return the step action dto
     */
    @JsonIgnore
    public StepActionDto log() {
        if (null != this) {
            final StringBuilder sb = new StringBuilder("StepAction - ");
            if (null != this.getStatus()) {
                sb.append("status:").append(this.getStatus()).append(" - ");
            }
            if (null != this.getMessage1()) {
                sb.append(" ").append(this.getMessage1());
            }
            if (null != this.getMessage2()) {
                sb.append(" ").append(this.getMessage2());
            }
            if (null != this.getMessage3()) {
                sb.append(" ").append(this.getMessage3());
            }
            TS.log().info(sb.toString());
        }
        return this;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status
     * @return the step action dto
     */
    public StepActionDto setStatus(final Boolean status) {
        this.status = status;
        return this;
    }

    /**
     * Gets the message1.
     *
     * @return the message1
     */
    public String getMessage1() {
        return message1;
    }

    /**
     * Sets the message1.
     *
     * @param message1 the message1
     * @return the step action dto
     */
    public StepActionDto setMessage1(final String message1) {
        this.message1 = message1;
        return this;
    }
}
