package com.madd.madd.gallery.Edition.Editor;

import java.util.List;

public interface EditorContract {

    interface View {

        List<String> getPictureList();
        int getSelectedPicture();
        void setSelectedPicture(int selectedPicture);

        void showEditionButton();
        void loadPicture(String path);
        void editPicture(String path);

        void returnSelectedPictureList();

        void refreshPictures(int fromPosition, int toPosition);
        void refreshPicture(int position);
        void refreshPictures();
    }

    interface Presenter {
        void setView(EditorContract.View view);

        void showPictureList();
        void selectPicture(int position);
        void editPicture(String path);
        void dragPicture(int fromPosition,int toPosition);

        void openPictureEditor();
        void returnSelectedPictureList();

        interface ReturnSelectedPictureList{
            void selectedPictureList(List<String> selectedPictureList);
        }
    }

    interface Model {

    }


}
