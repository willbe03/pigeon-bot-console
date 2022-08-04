package org.gugugu.quote

import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gugugu.config.Config

const val OCR_URL = "https://api.ocr.space/parse/imageurl"
const val OCR_LANG = "chs"

data class OCRData(
    val ParsedResults: List<PR>,
    val OCRExitCode: Int,
    val IsErroredOnProcessing: Boolean,
    val SearchablePDFURL: String,
    val ProcessingTimeInMilliseconds: String
) {
    data class PR(
        //var TextOverlay:String = "",
        val ParsedText: String,
        val TextOrientation: String,
        val FileParseExitCode: Int,
        val ErrorMessage: String,
        val ErrorDetails: String
    )

}

fun ocr(imageUrl: String): String {
//    val postBody = FormBody.Builder()
//        .add("apikey", Config.ocrApiKey)
//        .add("base64Image","data:image/jpg;base64,$imageBaseString")
//        .add("language",lang)
//        .build()

    val builder = OCR_URL.toHttpUrl().newBuilder()
    builder.addQueryParameter("apikey", Config.ocrApiKey)
    builder.addQueryParameter("url", imageUrl)
    builder.addQueryParameter("language", OCR_LANG)
    val url = builder.build()

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    val client = OkHttpClient()
    val response = client.newCall(request).execute().body!!.string()
    val gson = Gson()
    val result = gson.fromJson(response, OCRData::class.java)
    return result.ParsedResults[0].ParsedText
}