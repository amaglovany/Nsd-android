package com.amaglovany.nsd.presentation

import android.net.nsd.NsdServiceInfo
import com.amaglovany.nsd.repository.NsdInfoRepository

interface Contract {

    interface List {
        interface View : UpdatableView {
            fun setSubtitle(text: String?)
        }

        interface Presenter {
            val repository: NsdInfoRepository
            fun get(position: Int): NsdServiceInfo?
        }
    }

    interface Chat {
        interface View : PresentationView
    }
}