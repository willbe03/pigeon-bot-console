import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gugugu.PigeonBotConsole
import java.net.URL

const val SETU_API_ADDRESS = "https://api.lolicon.app/setu/v2"

object SetuCommand : SimpleCommand(PigeonBotConsole, "色图", description = "色图") {
    @Handler
    suspend fun randomSetu(context: CommandContext) {
        setu(context, null)
    }

    @Handler
    suspend fun searchSetu(context: CommandContext, tag: String){
        setu(context, tag);
    }

    suspend fun setu(context: CommandContext, tag: String?) {
        val client = OkHttpClient()
        //        val response = client.get(SETU_API_ADDRESS) {
//            if (tag != null) {
//                url {
//                    parameters.append("tag", tag);
//                }
//            }
//        }.body<Setu>()
        val urlBuilder = SETU_API_ADDRESS.toHttpUrl().newBuilder()
        urlBuilder.addQueryParameter("size", "regular")
        if (tag != null){
            urlBuilder.addQueryParameter("tag", tag)
            urlBuilder.addQueryParameter("num", "20")
        }
        val request = Request.Builder().url(urlBuilder.build()).build()
        val body = client.newCall(request).execute().body!!.string()
        val response  = Gson().fromJson<Setu>(body, Setu::class.java)

        if (response.error!! != "") {
            context.sender.sendMessage(response.error!!)
            return
        }
        if (response.data.size == 0) {
            context.sender.sendMessage("没有相关色图")
            return
        }
        val img = response.data.random()
        val imageUrl = img.urls?.regular
        if (imageUrl != null) {
                URL(imageUrl)
                    .openConnection()
                    .getInputStream()
                    .uploadAsImage(context.sender.subject!!)
                    .plus("https://www.pixiv.net/artworks/${img.pid}")
                    .sendTo(context.sender.subject!!)
        } else {
            context.sender.sendMessage("网络异常捏")
        }
    }
}

@kotlinx.serialization.Serializable
data class Setu (

    @SerializedName("error" ) var error : String?         = null,
    @SerializedName("data"  ) var data  : ArrayList<Data> = arrayListOf()

)

@kotlinx.serialization.Serializable
data class Urls (

    @SerializedName("original" ) var original : String? = null,
    @SerializedName("regular" ) var regular : String? = null,
    @SerializedName("small" ) var small : String? = null,
    @SerializedName("thumb" ) var thumb : String? = null,
    @SerializedName("mini" ) var mini : String? = null

)

@kotlinx.serialization.Serializable
data class Data (

    @SerializedName("pid"        ) var pid        : Int?              = null,
    @SerializedName("p"          ) var p          : Int?              = null,
    @SerializedName("uid"        ) var uid        : Int?              = null,
    @SerializedName("title"      ) var title      : String?           = null,
    @SerializedName("author"     ) var author     : String?           = null,
    @SerializedName("r18"        ) var r18        : Boolean?          = null,
    @SerializedName("width"      ) var width      : Int?              = null,
    @SerializedName("height"     ) var height     : Int?              = null,
    @SerializedName("tags"       ) var tags       : ArrayList<String> = arrayListOf(),
    @SerializedName("ext"        ) var ext        : String?           = null,
    @SerializedName("uploadDate" ) var uploadDate : Long?              = null,
    @SerializedName("urls"       ) var urls       : Urls?             = Urls()

)