package com.permissionx.joedev

import androidx.fragment.app.FragmentActivity

object PermissionX {
    private const val TAG = "InvisibleFragment"
    // FragmentActivity is parent class of AppCompatActivity
    fun request(activity: FragmentActivity, vararg permissions: String, callback:
    PermissionCallback) {
        val fragmentManager = activity.supportFragmentManager
        // to check if activity has fragment in TAG
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        // if it doesn't contain fragment, add InvisibleFragment. notice use commitNow.
        val fragment = if (existedFragment != null) {
            existedFragment as InvisibleFragment
        } else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        // *permissions means convert list to vararg to pass
        fragment.requestNow(callback, *permissions)
    }
}