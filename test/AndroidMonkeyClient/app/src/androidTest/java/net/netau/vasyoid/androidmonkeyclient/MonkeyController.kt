package net.netau.vasyoid.androidmonkeyclient

import android.graphics.Rect
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import org.json.JSONArray
import org.json.JSONObject

import khttp.post as httpPost

class MonkeyController(private val device: UiDevice) {

    fun generateAction(): Action {
        val headers = mapOf("Content-Type" to "application/json")
        val json = getElementsJSON()
        val response = httpPost(HOST, headers = headers, json = json)
        return Action(response.jsonObject)
    }

    private fun getElementsJSON(): JSONArray {
        val elements = device.findObjects(By.pkg(device.currentPackageName).clickable(true))
            .map { Element(it.visibleBounds) }
        val json = JSONArray()
        elements.forEach {
            json.put(it.toJSON())
        }
        return json
    }

    private data class Element(val position: Point, val size: Point) {

        fun toJSON(): JSONObject {
            return JSONObject(mapOf("position" to position.toJSON(), "size" to size.toJSON()))
        }

        constructor(bounds: Rect) : this(
            Point(bounds.left, bounds.top),
            Point(bounds.right - bounds.left, bounds.bottom - bounds.top)
        )
    }

    companion object {
        private const val HOST = "http://10.0.2.2:5000"
    }
}
