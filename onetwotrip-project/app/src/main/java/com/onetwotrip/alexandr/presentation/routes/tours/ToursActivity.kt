package com.onetwotrip.alexandr.presentation.routes.tours

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.presentation.ComponentsHolder
import com.onetwotrip.alexandr.presentation.di.AppComponent
import com.onetwotrip.alexandr.presentation.routes.base.MvpActivity
import com.onetwotrip.alexandr.presentation.utils.fadeIn
import com.onetwotrip.alexandr.presentation.utils.fadeOut
import kotlinx.android.synthetic.main.route_main.*
import kotlinx.android.synthetic.main.route_main_error.view.*
import java.lang.IllegalStateException

class ToursActivity : MvpActivity<ToursView, ToursPresenter>(), ToursView {

    companion object {
        private var KEY_VIEW_MODEL = "VIEW_MODEL"
    }

    override var viewModel: ToursViewModel?
        get() = _viewModel
        set(value) {
            val prevViewModel = _viewModel
            _viewModel = value
            if(value != null) applyViewModel(value, prevViewModel != null)
        }

    private var _viewModel: ToursViewModel? = null

    private lateinit var adapter: ToursAdapter

    private lateinit var _component: ToursComponent

    private fun applyViewModel(viewModel: ToursViewModel, withAnim: Boolean) {
        val animationDuration = 200L
        when(viewModel) {
            is ToursViewModel.Loading -> {
                loading_view.fadeIn(animationDuration, withAnim)
                error_view.fadeOut(animationDuration, withAnim)
                recycler_view.fadeOut(animationDuration, withAnim)
            }
            is ToursViewModel.Success -> {
                loading_view.fadeOut(animationDuration, withAnim)
                error_view.fadeOut(animationDuration, withAnim)
                recycler_view.fadeIn(animationDuration, withAnim)
                adapter.setData(viewModel.companies, viewModel.searchParams, viewModel.tours)
            }
            is ToursViewModel.Error -> {
                loading_view.fadeOut(animationDuration, withAnim)
                error_view.fadeIn(animationDuration, withAnim)
                recycler_view.fadeOut(animationDuration, withAnim)
            }
            else -> throw IllegalStateException("no case for view model type '$viewModel'")
        }
    }

    //region Mvp configuration
    override fun provideMvpView() = this
    override fun provideNewPresenter() = _component.presenter()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = ToursAdapter()
        recycler_view.adapter = adapter

        error_view.retry_button.setOnClickListener {
            presenter.loadData()
        }

        viewModel = savedInstanceState?.getParcelable(KEY_VIEW_MODEL)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_VIEW_MODEL, _viewModel)
        super.onSaveInstanceState(outState)
    }

    override fun configureDependencyInjection() {
        // persist Activity's component across orientation changes for cases
        // when component holds persistable instances
        // (not this app's case, but this is good for extensibility)

        // it also nice that one Activity can have multiple components
        if(!ComponentsHolder.any(viewId, ToursComponent::class)) {
            val component = DaggerToursComponent.builder()
                .appComponent(ComponentsHolder.getDefault<AppComponent>())
                .build()
            ComponentsHolder.add(viewId, ToursComponent::class, component)
        }
        _component = ComponentsHolder.get(viewId, ToursComponent::class)
    }

    override fun onResumeFreshActivity() {
        presenter.initializeViewModel()
        presenter.loadData()
    }

}
