package com.example.workflow.utils;

public class Constants {

    public static final String DELAYED = "delayed";
    public static final String GROUP_ID = "approvers";
    public static final String APPROVER_ID = "johnDoe";

    // NOTIFICATION TYPE
    public static final String NOTIFICATION_TYPE_APPROVED = "approved";
    public static final String NOTIFICATION_TYPE_DENIED = "denied";
    public static final String NOTIFICATION_TYPE_DELAYED = "delayed";

    // FLOW TYPE
    public static final String FLOW_TYPE_AUTOMATIC = "automatic";
    public static final String FLOW_TYPE_MANUAL = "MANUAL";

    // MAIN BPMN PROCESS ID
    public static final String START_PROCESS_ID = "main-flow";

    // NAMED BEAN NAME
    public static final String ADJUSTMENTS_STEP_BEAN_NAME = "ajustmentsStep";
    public static final String DELAYED_APPROVAL_STEP_BEAN_NAME = "delayedApprovalStep";
    public static final String NOTIFICATION_STEP_BEAN_NAME = "notificationStep";
    public static final String QUOTE_STEP_BEAN_NAME = "quoteStep";
    public static final String STOP_STEP_BEAN_NAME = "stopStep";

    // CAMUNDA VARIABLES
    public static final String VARIABLE_NOTIFICATION_CHECKPOINT = "travelCheckpoint";
    public static final String VARIABLE_RESULT_CHECKPOINT = "result";
    public static final String VARIABLE_QUOTE_REFERENCE_ID = "quoteReferenceId";
    public static final String VARIABLE_MANUAL_QUOTE_REFERENCE_ID = "manualQuoteReferenceId";
    public static final String VARIABLE_EMAIL_CUSTOMER = "email";
    public static final String CAMUNDA_VARIABLE_TRAVELER_NAME = "travelerName";
    public static final String CAMUNDA_VARIABLE_EMAIL = "email";
    public static final String CAMUNDA_VARIABLE_AMOUNT = "amount";
    public static final String CAMUNDA_VARIABLE_QUOTE_REFERENCE_ID = "quoteReferenceId";

    // ERROR CODES
    public static final String QUOTE_API_ERROR_CODE = "quote_api_exception";

}
