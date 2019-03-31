package net.netau.vasyoid.androidmonkeyclient

import androidx.test.uiautomator.UiDevice
import org.json.JSONObject

class Action(val type: Type, val position: Point) {

    constructor(action: JSONObject) : this(
        Type.valueOf(action.getString("type")), Point(action.getJSONObject("position"))
    )

    fun perform(device: UiDevice) {
        when (type) {
            Type.TAP -> device.click(position.x, position.y)
        }
    }

    enum class Type {
        TAP
    }
}
