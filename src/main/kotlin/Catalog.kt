import org.bson.types.ObjectId
import rx.Observable

class Catalog {
    private val catalogDataAccess = CatalogDataAccess()

    fun addUser(name: String, currency: String): Observable<String> {
        val id = ObjectId().toString()
        return catalogDataAccess.addUser(User(id, name, Currency.valueOf(currency)).toDocument()).map { id }
    }

    fun addProduct(name: String, price: Double): Observable<String> {
        val id = ObjectId().toString()
        return catalogDataAccess.addProduct(Product(id, name, price).toDocument()).map { id }
    }

    fun getUser(id: String): Observable<String> {
        return catalogDataAccess.getUser(id).map { it.toString() }
    }

    fun getProducts(userId: String): Observable<String> {
        return catalogDataAccess.getUser(userId)
            .flatMap { user -> catalogDataAccess.getProducts().map { it.toString(user.currency) } }
    }
}