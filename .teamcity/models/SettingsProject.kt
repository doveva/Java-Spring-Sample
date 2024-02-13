package models

open class SettingsProject (val name: String?, val description: String = "Test Description") {

    fun testFun(): String{
        if (name == null){
            return "No none in class"
        }
        return name as String
    }
}