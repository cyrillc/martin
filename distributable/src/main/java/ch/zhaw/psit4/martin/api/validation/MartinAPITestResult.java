package ch.zhaw.psit4.martin.api.validation;

/**
 * The return values for test results.
 * 
 * OK: Tests successful
 * WARNING: Tests successful, but might not use some functionality.
 * ERROR: Tests unsuccessful
 * 
 * @version 0.0.1-SNAPSHOT
 */
public enum MartinAPITestResult {
    OK, WARNING, ERROR
}
