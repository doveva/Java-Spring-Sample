package models

open class SettingsProject{
    var name: String? = null
    var description: String = "Test Description"

    fun testFun(): String{
        if (name == null){
            return "No none in class"
        }
        return name as String
    }
}