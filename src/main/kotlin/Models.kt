import org.bson.Document

enum class Currency {
    RUB, USD, EUR
}

interface MongoObject {
    fun toDocument(): Document
}

class User(private val id: String?, private val name: String, val currency: Currency): MongoObject {
    constructor(document: Document) : this(document["_id"] as String, document["name"] as String, Currency.valueOf(document["currency"] as String))

    override fun toDocument(): Document {
        val document = Document()
        document.append("_id", id)
        document.append("name", name)
        document.append("currency", currency.toString())
        return document
    }

    override fun toString(): String {
        return """
            User {
                id: $id,
                name: $name,
                currency: $currency
            }
        """.trimIndent()
    }
}

class Product(private val id: String?, private val name: String, private val priceInUSD: Double): MongoObject {
    constructor(document: Document) : this(document["_id"] as String, document["name"] as String, document["price"] as Double)

    override fun toDocument(): Document {
        val document = Document()
        document.append("_id", id)
        document.append("name", name)
        document.append("price", priceInUSD)
        return document
    }

    fun toString(currency: Currency): String {
        return """
            Product {
                id: $id,
                name: $name,
                price: ${usdToCurrency(priceInUSD, currency)}
            }
        """.trimIndent()
    }
}