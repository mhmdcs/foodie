package com.example.foodie.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.foodie.models.FoodRecipe
import com.example.foodie.models.Result

class RecipesDiffUtil(private val oldlist: List<Result>, private val newList: List<Result>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldlist.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldlist[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition] == newList[newItemPosition]
    }
}