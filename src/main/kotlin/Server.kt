import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

class Server {
    val catalog = Catalog()

    private fun HttpServerRequest<ByteBuf>.getParameter(name: String): String {
        return this.queryParameters.getOrDefault(name, null)?.getOrNull(0)
            ?: throw CatalogError("parameter $name expected")
    }

    fun process(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
        try {
            val message = when (request.decodedPath) {
                "/addUser" -> catalog.addUser(request.getParameter("name"), request.getParameter("currency"))
                "/getUser" -> catalog.getUser(request.getParameter("id"))
                "/addProduct" -> catalog.addProduct(
                    request.getParameter("name"),
                    request.getParameter("price").toDouble()
                )
                "/getProducts" -> catalog.getProducts(request.getParameter("userId"))
                else -> {
                    throw CatalogError("unsupported method")
                }
            }
            response.status = HttpResponseStatus.OK
            return response.writeString(message.map { it + "\n" })
        } catch (e: Exception) {
            response.status = HttpResponseStatus.BAD_REQUEST
            return response.writeString(Observable.just(e.message))
        }
    }
}

fun main() {
    val server = Server()

    HttpServer
        .newServer(8081)
        .start { request, response -> server.process(request, response) }
        .awaitShutdown()
}