
import play.mvc.*;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import utils.ApiResponse;

/**
 * Base controller to standardize API responses for Play Framework.
 */
public abstract class AbstractController extends Controller {

    protected CompletionStage<Result> okWithData(Object data) {
        return CompletableFuture.completedFuture(
            Results.ok(Json.toJson(ApiResponse.success(data)))
        );
    }

    protected CompletionStage<Result> okWithMessage(Object data, String message) {
        return CompletableFuture.completedFuture(
            Results.ok(Json.toJson(ApiResponse.success(message, data)))
        );
    }

    protected CompletionStage<Result> createdWithData(Object data) {
        return CompletableFuture.completedFuture(
            Results.created(Json.toJson(ApiResponse.success("Resource created successfully", data)))
        );
    }

    protected CompletionStage<Result> createdWithMessage(Object data, String message) {
        return CompletableFuture.completedFuture(
            Results.created(Json.toJson(ApiResponse.success(message, data)))
        );
    }

    protected CompletionStage<Result> noContentResponse() {
        return CompletableFuture.completedFuture(
            Results.noContent()
        );
    }

    protected CompletionStage<Result> badRequestWithMessage(String message) {
        return CompletableFuture.completedFuture(
            Results.badRequest(Json.toJson(ApiResponse.error(message)))
        );
    }

    protected CompletionStage<Result> notFoundWithMessage(String message) {
        return CompletableFuture.completedFuture(
            Results.notFound(Json.toJson(ApiResponse.error(message)))
        );
    }

    protected CompletionStage<Result> internalErrorWithMessage(String message) {
        return CompletableFuture.completedFuture(
            Results.internalServerError(Json.toJson(ApiResponse.error(message)))
        );
    }

    protected void logAndRethrow(Exception e, String msg) {
        play.Logger.error(msg, e);
        throw new RuntimeException(msg, e);
    }
} 