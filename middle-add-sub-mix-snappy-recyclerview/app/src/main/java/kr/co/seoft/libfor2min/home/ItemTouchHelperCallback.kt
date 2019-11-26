package kr.co.seoft.libfor2min.home

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.libfor2min.i


// ref : http://dudmy.net/android/2018/05/02/drag-and-swipe-recyclerview , https://github.com/dudmy/blog-sample
class ItemTouchHelperCallback(
    val moveCb : (RecyclerView.ViewHolder, RecyclerView.ViewHolder)->Unit,
    val endCb : (Int)->Unit)
    : ItemTouchHelper.Callback(){

    var isMoved = false

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return makeMovementFlags(dragFlags,0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        "target $target".i()

        moveCb.invoke(viewHolder, target)
        isMoved = true
        return true
    }

    /**
     * call for refresh badge's bottom numbers
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if(isMoved) {
            endCb.invoke(0)
            isMoved = false
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun isLongPressDragEnabled() = false

}