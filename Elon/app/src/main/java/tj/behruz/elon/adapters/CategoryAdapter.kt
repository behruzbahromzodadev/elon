package tj.behruz.elon.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import tj.behruz.elon.models.Category

class CategoryAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val categories: List<Category>): ArrayAdapter<Category>(context, layoutResource, categories) {


    override fun getCount(): Int {
        return categories.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(
            layoutResource,
            parent,
            false
        ) as TextView
        view.text = categories[position].name
        return view
    }


}