package com.madd.madd.gallery.UI.Fragments.Album;

import java.util.List;

public class AlbumPresenter implements AlbumContract.Presenter {

    private AlbumContract.View view;
    private AlbumContract.Model model;


    public AlbumPresenter(AlbumContract.Model model) {
        this.model = model;
    }


    @Override
    public void setView(AlbumContract.View view) {
        this.view = view;
    }

    @Override
    public void requestPictureList(String albumName) {
        if( view != null ){
            view.showLoadingProgress();
            model.getPictureList(albumName, new AlbumContract.Model.PictureListRequest() {
                @Override
                public void onSuccess(List<String> pictureList) {
                    if( view != null ){
                        view.showPictureList(view.getSelectedPictureList(), pictureList);
                        view.showSelectedPictureCounter(view.getSelectedPictureList().size());
                        view.hideLoadingProgress();
                    }
                }

                @Override
                public void onError(String error) {
                    if( view != null ){
                        view.showEmptyListError();
                        view.hideLoadingProgress();
                    }
                }
            });

        }
    }








    @Override
    public void selectPicture(String selectedPath, int selectedPosition) {
        if( view != null ){
            if( view.getMultipleSelection() ){
                multipleSelection(selectedPath, selectedPosition);
            } else {
                singleSelection(selectedPath, selectedPosition);
            }
        }
    }



    private void multipleSelection(String selectedPath, int selectedPosition ){

        List<String> selectedPictureList = view.getSelectedPictureList();

        int index = selectedPictureList.indexOf(selectedPath);
        // Select item
        if ( index == -1 ) {
            // Under limit
            if ( selectedPictureList.size() < 10 ){
                selectedPictureList.add(selectedPath);
            }
            // Unselect item
        } else {
            selectedPictureList.remove(index);
        }

        view.showSelectedPictureCounter(selectedPictureList.size());
        view.refreshPictureAtPosition(selectedPosition);

    }


    private void singleSelection(String selectedPath, int selectedPosition){

        List<String> selectedPictureList = view.getSelectedPictureList();
        List<String> albumPictureList = view.getAlbumPictureList();

        int index = selectedPictureList.indexOf(selectedPath);
        // Exist on list
        if ( index != -1 ) {
            selectedPictureList.remove(index);
            // Does not exist on list
        } else {
            // Currently there is a selected item
            if (selectedPictureList.size() > 0 ) {
                // Delete current item
                String currentPath = selectedPictureList.get(0);
                selectedPictureList.clear();
                index = albumPictureList.indexOf(currentPath);
                if ( index != -1 ) {
                    view.refreshPictureAtPosition(index);
                }
            }
            selectedPictureList.add(selectedPath);
        }

        view.showSelectedPictureCounter(selectedPictureList.size());
        view.refreshPictureAtPosition(selectedPosition);
    }




    @Override
    public void returnSelectedPictureList() {
        if( view != null ) {
            view.returnSelectedPictureList(view.getSelectedPictureList());
        }
    }



    public interface ReturnSelectedPictureList{
        void selectedPictureList(List<String> selectedPictureList);
    }

}
