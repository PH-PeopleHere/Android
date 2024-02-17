package com.peopleHere.people_here.Remote

import com.peopleHere.people_here.Data.MainData

interface RequestEnjoyView{
    fun RequestEnjoyLoading()
    fun RequestEnjoySuccess(content : RequestEnjoyResponse)
    fun RequestEnjoyFailure(status : Int, message : String)
}