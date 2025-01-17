package dev.jimboslice.filemanager.adapters

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter
import com.simplemobiletools.commons.extensions.getColoredDrawableWithColor
import com.simplemobiletools.commons.extensions.getTextSize
import com.simplemobiletools.commons.extensions.getTimeFormat
import com.simplemobiletools.commons.helpers.getFilePlaceholderDrawables
import com.simplemobiletools.commons.views.MyRecyclerView
import dev.jimboslice.filemanager.R
import dev.jimboslice.filemanager.activities.SimpleActivity
import dev.jimboslice.filemanager.extensions.config
import dev.jimboslice.filemanager.models.ListItem
import kotlinx.android.synthetic.main.item_file_dir_list.view.*
import java.util.*

class DecompressItemsAdapter(activity: SimpleActivity, var listItems: MutableList<ListItem>, recyclerView: MyRecyclerView, itemClick: (Any) -> Unit) :
    MyRecyclerViewAdapter(activity, recyclerView, itemClick) {

    private lateinit var fileDrawable: Drawable
    private lateinit var folderDrawable: Drawable
    private var fileDrawables = HashMap<String, Drawable>()
    private var fontSize = 0f
    private var smallerFontSize = 0f
    private var dateFormat = ""
    private var timeFormat = ""

    init {
        initDrawables()
        fontSize = activity.getTextSize()
        smallerFontSize = fontSize * 0.8f
        dateFormat = activity.config.dateFormat
        timeFormat = activity.getTimeFormat()
    }

    override fun getActionMenuId() = 0

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = 0

    override fun getIsItemSelectable(position: Int) = false

    override fun getItemSelectionKey(position: Int) = 0

    override fun getItemKeyPosition(key: Int) = 0

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createViewHolder(R.layout.item_decompression_list_file_dir, parent)

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.ViewHolder, position: Int) {
        val fileDirItem = listItems[position]
        holder.bindView(fileDirItem, true, false) { itemView, layoutPosition ->
            setupView(itemView, fileDirItem)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = listItems.size

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (!activity.isDestroyed && !activity.isFinishing) {
            val icon = holder.itemView.item_icon
            if (icon != null) {
                Glide.with(activity).clear(icon)
            }
        }
    }

    private fun setupView(view: View, listItem: ListItem) {
        view.apply {
            val fileName = listItem.name
            item_name.text = fileName
            item_name.setTextColor(textColor)
            item_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

            if (listItem.isDirectory) {
                item_icon.setImageDrawable(folderDrawable)
            } else {
                val drawable = fileDrawables.getOrElse(fileName.substringAfterLast(".").toLowerCase(), { fileDrawable })
                val options = RequestOptions()
                    .signature(listItem.getKey())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(drawable)
                    .centerCrop()

                val itemToLoad = getImagePathToLoad(listItem.path)
                if (!activity.isDestroyed) {
                    Glide.with(activity)
                        .load(itemToLoad)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(options)
                        .into(item_icon)
                }
            }
        }
    }

    private fun getImagePathToLoad(path: String): Any {
        return if (path.endsWith(".apk", true)) {
            val packageInfo = activity.packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null) {
                val appInfo = packageInfo.applicationInfo
                appInfo.sourceDir = path
                appInfo.publicSourceDir = path
                appInfo.loadIcon(activity.packageManager)
            } else {
                path
            }
        } else {
            path
        }
    }

    private fun initDrawables() {
        folderDrawable = resources.getColoredDrawableWithColor(R.drawable.ic_folder_vector, textColor)
        folderDrawable.alpha = 180
        fileDrawable = resources.getDrawable(R.drawable.ic_file_generic)
        fileDrawables = getFilePlaceholderDrawables(activity)
    }
}
