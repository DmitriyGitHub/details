package ru.good.di.likeit.screens.general.persons;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.good.di.likeit.R;
import ru.good.di.likeit.model.Person;

public class PersonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Person> mListPersons;

    /**
     * Footer progress
     */
    private RecyclerView mRecyclerView;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreInterface mOnLoadMoreListener;
    private boolean isLoading;
    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;

    public PersonListAdapter(List<Person> persons, RecyclerView recyclerView){
        mListPersons = persons;

        mRecyclerView = recyclerView;

        /**
         * Footer progress
         */
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    public void changeDataSet(@NonNull List<Person> persons) {

        mListPersons.addAll(persons);

        notifyDataSetChanged();

        notifyItemChanged(1);
    }

    /**
     *      Метод onCreateViewHolder вызывается виджетом RecyclerView, когда ему потре-
     *  буется новое представление для отображения элемента. В этом методе мы создаем
     *  объект View и упаковываем его в ViewHolder. RecyclerView пока не ожидает, что
     *  представление будет связано с какими-либо данными.
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // загрузка Item
        if (viewType == VIEW_TYPE_ITEM) {
		
            View view = layoutInflater
                    .inflate(R.layout.persons_item_person, parent, false);

            return new PersonListViewHolder(view);
		
        }else if (viewType == VIEW_TYPE_LOADING){
		
            View view1 = layoutInflater
                    .inflate(R.layout.layout_loading_item, parent, false);

            return new PersonListLoadingViewHolder(view1);
        }
        return null;
    }


    /**
     * Этот метод связывает представление View объекта ViewHolder с объектом модели.
     * При вызове он получает ViewHolder и позицию
     * в наборе данных. Позиция используется для нахождения правильных данных мо-
     * дели, после чего View обновляется в соответствии с этими данными.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int getItemType = holder.getItemViewType();
        if (getItemType == VIEW_TYPE_ITEM) {
        /**
         * В данной реализации эта позиция определяет индекс объекта Person в массиве.
         */
            Person person = mListPersons.get(position);

        /**
         * После получения нужного объекта мы связываем всё с View, присваивая значиния виджетам
         *  из ViewHolder
         */
            PersonListViewHolder personListViewHolder = (PersonListViewHolder) holder;
            personListViewHolder.bindPhoto(person);


        }else if (getItemType == VIEW_TYPE_LOADING){
            PersonListLoadingViewHolder listLoadingViewHolder = (PersonListLoadingViewHolder) holder;
            listLoadingViewHolder.mProgressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemCount() {
        /**
         * Footer progress
         */
        return (mListPersons != null) ? mListPersons.size() : 0;
    }

    /**
     * Footer progress
     */
    @Override
    public int getItemViewType(int position) {
        return mListPersons.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }
    public void setOnLoadMoreListener(OnLoadMoreInterface onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

}