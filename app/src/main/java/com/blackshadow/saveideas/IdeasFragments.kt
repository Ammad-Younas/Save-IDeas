import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.blackshadow.saveideas.IdeasAdapter
import com.blackshadow.saveideas.IdeasModel
import com.blackshadow.saveideas.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackshadow.saveideas.DatabaseHelper

class IdeasFragments : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IdeasAdapter
    private val ideasList = mutableListOf<IdeasModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ideas, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = IdeasAdapter(ideasList, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadIdeas()

        return view
    }

    private fun loadIdeas() {
        val dbHelper = DatabaseHelper(requireContext())
        val ideas = dbHelper.getAllIdeas()

        ideasList.clear()
        ideasList.addAll(ideas)
        adapter.notifyDataSetChanged()
    }

    fun addNewIdea(title: String, description: String) {
        val dbHelper = DatabaseHelper(requireContext())
        val newIdeaId = dbHelper.insertIdea(title, description)

        val newIdea = IdeasModel(newIdeaId.toInt(), title, description, R.drawable.baseline_delete)
        ideasList.add(newIdea)
        adapter.notifyItemInserted(ideasList.size - 1)
        recyclerView.scrollToPosition(ideasList.size - 1)
    }
}
