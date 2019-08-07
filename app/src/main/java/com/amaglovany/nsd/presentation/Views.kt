package com.amaglovany.nsd.presentation

interface PresentationView

interface UpdatableView : PresentationView {
    fun update()
    fun onItemInserted(position: Int)
    fun onItemRemoved(position: Int)
}