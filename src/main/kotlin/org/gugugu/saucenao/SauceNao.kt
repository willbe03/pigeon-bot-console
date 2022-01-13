package org.gugugu.saucenao

data class SauceNao(
    val header: Header,
    val results: MutableList<SauceNaoResult>
)

data class Data(
    val ext_urls: List<String>,
    val title: String,
    //pixiv:
    val pixiv_id: Int?,
    val member_name: String?,
    val member_id: Int?,
    // twitter:
    val tweet_id: String?,
    val twitter_user_id: String?,
    val twitter_user_handle: String?,
    // yandere:
    val yandere_id: Int?,
    val creater: String?,
    val material: String?,
    val characters: String?,
    val source: String
)

data class Header(
    val account_type: String,
    val index: Any,
    val long_limit: String,
    val long_remaining: Int,
    val minimum_similarity: Double,
    val query_image: String,
    val query_image_display: String,
    val results_requested: Int,
    val results_returned: Int,
    val search_depth: String,
    val short_limit: String,
    val short_remaining: Int,
    val status: Int,
    val user_id: String
)

data class SauceNaoResult(
    val `data`: Data,
    val header: SauceNaoResultInHeader
)

data class SauceNaoResultInHeader(
    val dupes: Int,
    val hidden: Int,
    val index_id: Int,
    val index_name: String,
    val similarity: String,
    val thumbnail: String
)