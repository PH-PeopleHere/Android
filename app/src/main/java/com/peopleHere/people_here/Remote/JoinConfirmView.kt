package com.peopleHere.people_here.Remote

interface JoinConfirmView {
    fun JoinConfirmLoading()
    fun JoinConfirmSuccess(content : JoinConfirmResponse)
    fun JoinConfirmFialure(status : Int, message : String)
}