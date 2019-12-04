package com.madd.madd.gallery.UserGallery.Gallery;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.madd.madd.gallery.Root.App;
import com.madd.madd.gallery.UserGallery.Album.AlbumFragment;
import com.madd.madd.gallery.UserGallery.Gallery.GalleryContract;
import com.madd.madd.gallery.UserGallery.Gallery.Album;
import com.madd.madd.gallery.UserGallery.Album.AlbumPresenter;
import com.madd.madd.gallery.UserGallery.Gallery.GalleryPresenter;
import com.madd.madd.gallery.R;
import com.madd.madd.gallery.UserGallery.Gallery.AlbumAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements GalleryContract.View {


    @Inject
    GalleryContract.Presenter presenter;

    @BindView(R.id.PB_Gallery)  ProgressBar progressBar;
    @BindView(R.id.TV_Gallery_Message)  TextView textViewMessage;
    @BindView(R.id.BTN_Gallery_Permission)  Button buttonPermission;
    @BindView(R.id.CTNR_Gallery)  RecyclerView recyclerView;
    @BindView(R.id.BTN_Gallery_Accept)  Button buttonAccept;

    public GalleryFragment() {}

    private AlbumAdapter adapter;

    private List<Album> albumList = new ArrayList<>();
    private boolean multipleSelection = false;
    private AlbumPresenter.ReturnSelectedPictureList returnSelectedPictureList;
    private List<String> selectedPictureList = new ArrayList<>();


    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }
    public void getSelectedPictureList(AlbumPresenter.ReturnSelectedPictureList returnSelectedPictureList) {
        this.returnSelectedPictureList = returnSelectedPictureList;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this,v);
        ((App) Objects.requireNonNull(getActivity()).getApplication()).getComponent().inject(this);

        loadView();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.requestAlbumList();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onPermissionsResult(grantResults);
    }








    private void loadView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager( new GridLayoutManager(getContext(), 2));

        adapter = new AlbumAdapter( albumList, albumName ->
            presenter.openAlbum(albumName)
        );
        recyclerView.setAdapter(adapter);

        buttonPermission.setOnClickListener(view ->
            presenter.requestAlbumList()
        );

        buttonAccept.setOnClickListener(view -> {
            presenter.returnSelectedPictureList();
        });
    }





    @Override
    public void showAlbumList(List<Album> pAlbumList) {
        albumList.clear();
        albumList.addAll(pAlbumList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyListError() {
        textViewMessage.setText("No hay albums en tu galería");
        textViewMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPermissionError() {
        textViewMessage.setText("Concede permisos para acceder a tu galería");
        textViewMessage.setVisibility(View.VISIBLE);
        buttonPermission.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePermissionError() {
        textViewMessage.setVisibility(View.GONE);
        buttonPermission.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSelectedPictureCounter(int counter) {
        buttonAccept.setText("Ok(" + String.valueOf(counter) + ")");
    }

    @Override
    public void openAlbum(List<String> selectedPictureList, boolean multipleSelection, String albumName) {
        AlbumFragment album = new AlbumFragment();
        album.setMultipleSelection(multipleSelection);
        album.setSelectedPictureList(selectedPictureList);
        album.setAlbumName(albumName);
        album.getSelectedPictureList(updatedSelectedPictureList -> {
            presenter.updateSelectedPictureList(updatedSelectedPictureList);
        });
        getFragmentManager().beginTransaction().replace(R.id.CTNR_Main,album).addToBackStack(null).commit();
    }



    @Override
    public void returnSelectedPictureList(List<String> selectedPictureList) {
        returnSelectedPictureList.selectedPictureList(selectedPictureList);
    }






    @Override
    public List<String> getSelectedPictureList() {
        return selectedPictureList;
    }

    @Override
    public boolean getMultipleSelection() {
        return multipleSelection;
    }

    @Override
    public Fragment getViewFragment() {
        return this;
    }


}
