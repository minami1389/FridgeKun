package com.example.minami1389.fridgekun.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.minami1389.fridgekun.Activity.FridgeActivity
import com.example.minami1389.fridgekun.Model.Item
import com.example.minami1389.fridgekun.R
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


class AddItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fragment_add_item, container, false)

        rootView.findViewById(R.id.decideItemButton).setOnClickListener {
            val userName = FirebaseAuth.getInstance().currentUser?.displayName
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val name = (rootView.findViewById(R.id.itemNameEditText) as EditText).text.toString()
            val expired = (rootView.findViewById(R.id.itemExpiredEditText) as EditText).text.toString()
            Item(name, userId.toString(), userName.toString(), expired).writeNewItem((activity as FridgeActivity).name)
            getFragmentManager().beginTransaction().remove(this).commit()
        }

        val itemExpiredEditText = rootView.findViewById(R.id.itemExpiredEditText) as EditText
        itemExpiredEditText.setOnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.onDateSetCallback = { year, month, day ->
                itemExpiredEditText.setText(year.toString() + "年" + month.toString() + "月" + day.toString() + "日")
            }
            datePicker.show(activity.supportFragmentManager, "datePicker")
        }

        return rootView
    }

    fun stringToDate(date: String): Date {
        return SimpleDateFormat("yyyy年MM月dd日").parse(date)
    }

}