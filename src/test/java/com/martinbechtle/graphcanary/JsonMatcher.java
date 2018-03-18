package com.martinbechtle.graphcanary;

import com.jayway.restassured.response.ValidatableResponse;
import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A Hamcrest matcher for matching JSON in JSON resource files.
 *
 * @author Rafael Tedin Alvarez
 */
public class JsonMatcher extends BaseMatcher<ValidatableResponse> {

    private final Class<?> callerClass;
    private String expected;
    private List<Customization> customizations;

    /**
     * @param callerClass The class that will call the matcher.
     */
    public JsonMatcher(Class<?> callerClass) {

        this.callerClass = callerClass;
        this.customizations = new ArrayList<>();
    }

    @Override
    public boolean matches(Object item) {

        if (item instanceof String) {
            String actual = (String) item;
            try {
                JSONAssert.assertEquals(
                        expected,
                        actual,
                        new CustomComparator(
                                JSONCompareMode.STRICT,
                                customizations.toArray(new Customization[0])));
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {

        description.appendText(expected);
    }

    /**
     * @param jsonResourceFile The JSON resource file that this matcher will match against.
     * @return A matcher for matching JSON.
     * @throws IOException If the resource file cannot be loaded.
     */
    public JsonMatcher forFile(String jsonResourceFile) throws IOException {

        expected = loadFromClasspath(jsonResourceFile, callerClass);
        return this;
    }

    /**
     * @param jsonPath JSON path to ignore.
     * @return A matcher for matching JSON in JSON resource files ignoring the supplied JSON path.
     */
    public JsonMatcher ignoring(String jsonPath) {

        Customization customization = Customization.customization(jsonPath, this::ignore);
        customizations.add(customization);

        return this;
    }

    /**
     * @param jsonPath JSON path that must not be {@code null}.
     * @return A matcher for matching JSON in JSON resource files expecting a non null value at the supplied path.
     */
    public JsonMatcher expectingNotNull(String jsonPath) {

        Customization customization = Customization.customization(jsonPath, this::notNull);
        customizations.add(customization);

        return this;
    }

    /**
     * Constructs a JSON matcher.
     *
     * @param callerClass      The class that will call the matcher.
     * @param jsonResourceFile The JSON resource file that the returned matcher will match against.
     * @return A matcher for matching JSON.
     * @throws IOException
     */
    public static JsonMatcher hasSameContentAs(Class<?> callerClass, String jsonResourceFile) throws IOException {

        return new JsonMatcher(callerClass).forFile(jsonResourceFile);
    }

    /**
     * Constructs a JSON matcher.
     * <p>
     * The method makes an effort to guess the class that will call the matcher. It is assumed that it will be the
     * first class in the stack trace that ends with "Test" (i.e. normally the test class that will use the matcher).
     *
     * @param jsonResourceFile The JSON resource file that the returned matcher will match against.
     * @return A matcher for matching JSON.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static JsonMatcher hasSameContentAs(String jsonResourceFile) throws IOException, ClassNotFoundException {

        String callerClassName = null;

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().endsWith("Test")) {
                callerClassName = element.getClassName();
                break;
            }
        }

        Class<?> callerClass = Class.forName(callerClassName);

        return new JsonMatcher(callerClass).forFile(jsonResourceFile);
    }

    private String loadFromClasspath(String jsonFile, Class<?> callerClass) throws IOException {

        try (InputStream res = callerClass.getResourceAsStream(jsonFile)) {

            if (res == null) {
                String packageName = callerClass.getPackage().getName();
                throw new IOException(String.format("File [%s] not found in package [%s]", jsonFile, packageName));
            }
            return IOUtils.toString(res, "UTF-8");
        }
    }

    private <T> boolean notNull(T expected, T actual) {

        return !JSONObject.NULL.equals(expected);
    }

    private <T> boolean ignore(T expected, T actual) {

        return true;
    }
}