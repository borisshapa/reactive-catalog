import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import org.bson.Document
import rx.Observable

class CatalogDataAccess {
    companion object {
        val PORT = 27017
        val CLIENT = createMongoClient()
        private fun createMongoClient(): MongoClient {
            return MongoClients.create("mongodb://localhost:$PORT")
        }
    }

    private fun db(): MongoDatabase {
        return CLIENT.getDatabase("catalog")
    }

    private fun users(): MongoCollection<Document> {
        return db().getCollection("users")
    }

    private fun products(): MongoCollection<Document> {
        return db().getCollection("products")
    }

    fun addUser(user: Document): Observable<Success> {
        return users().insertOne(user)
    }

    fun addProduct(product: Document): Observable<Success> {
        return products().insertOne(product)
    }

    fun getUser(id: String): Observable<User> {
        return users().find().toObservable().filter { it["_id"] == (id) }.map { User(it) }
    }

    fun getProducts(): Observable<Product> {
        return products().find().toObservable().map { Product(it) }
    }
}