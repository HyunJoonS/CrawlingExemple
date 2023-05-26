package com.example.kotlinCrawling

import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KotlinCrawlingApplicationTests {

	@Test
	fun detailSearch() {
		var biznum = "2768801550"
		var url = Jsoup.connect("https://moneypin.biz/bizno/detail/$biznum").get()

		val data = url.select("#ceo")
		if(data.isEmpty()){
			println("조회 실패")
		}
		else{
			println("data = $data")
			val split = url.title().split("-")
			println("title = ${split[1].trim()}")
			println("element = ${data[0].text()}")
		}
	}

	@Test
	fun jsonSearch() {
		var biznum = "1046100318"
		var url = Jsoup.connect("https://moneypin.biz/bizno/?name=$biznum").get()

		val data = url.select("#__NEXT_DATA__").toString()
		val startIndex = data.indexOf("defaultData\":") + "defaultData\":".length
		val endIndex = data.lastIndexOf("},\"__N_SSP\"")
		val jsonString = data.substring(startIndex, endIndex)

		val jsonArray = JSONArray(jsonString)
		val defaultDataList = mutableListOf<DefaultData>()

		for (i in 0 until jsonArray.length()) {
			if (i >= 3) break  // 세 개의 데이터만 담기 위해 반복문 종료

			val jsonObject = jsonArray.getJSONObject(i)
			val cmpName = jsonObject.getString("cmpName")
			val bizNo = jsonObject.getString("bizNo")
			val rprsnName = jsonObject.getString("rprsnName")
			val address = jsonObject.getString("address")
			val roadNameAddr = jsonObject.getString("roadNameAddr")

			val defaultData = DefaultData(cmpName, bizNo, rprsnName, address, roadNameAddr)
			defaultDataList.add(defaultData)
		}

		defaultDataList.forEach { println(it) }
	}

	@Test
	fun main() {
		val jsonString = """[
        {"cmpName":"주식회사 클리카","bizNo":"6568801983","rprsnName":"김나율","address":"광주광역시 동구 동명동 143-78 I-PLEX 광주 300호","roadNameAddr":"광주광역시 동구 동계천로 150, 300호(동명동, I-PLEX 광주)"},
        {"cmpName":"주식회사 엘빈","bizNo":"3728101655","rprsnName":"윤예진","address":"경기도 의정부시 가능동 727-10  3층","roadNameAddr":"경기도 의정부시 가능로50번길 13-26, 3층(가능동)"},
        {"cmpName":"에이치글로벌","bizNo":"4760801783","rprsnName":"허훈","address":"경기도 고양시 덕양구 원흥동 705  고양원흥 한일윈스타 지식산업센터 제11층 1114호","roadNameAddr":"경기도 고양시 덕양구 삼원로 73, 고양원흥 한일윈스타 지식산업센터 제11층 1114호(원흥동)"}
    ]""".trimIndent()

		val jsonArray = JSONArray(jsonString)

		val defaultDataList = mutableListOf<DefaultData>()

		for (i in 0 until jsonArray.length()) {
			if (i >= 3) break  // 세 개의 데이터만 담기 위해 반복문 종료

			val jsonObject = jsonArray.getJSONObject(i)
			val cmpName = jsonObject.getString("cmpName")
			val bizNo = jsonObject.getString("bizNo")
			val rprsnName = jsonObject.getString("rprsnName")
			val address = jsonObject.getString("address")
			val roadNameAddr = jsonObject.getString("roadNameAddr")

			val defaultData = DefaultData(cmpName, bizNo, rprsnName, address, roadNameAddr)
			defaultDataList.add(defaultData)
		}

		defaultDataList.forEach { println(it) }
	}

	data class DefaultData(
		val companyName: String,
		val bizNo: String,
		val ceoName: String,
		val address: String,
		val address2: String
	)

}
