package com.babbangona.shpincentiveapp;

public class READ_ME_DOCUMENTATION {

    //+++++++++++ VERSION 1.0.0 +++++++++++
    /**
     * Version 1.1.0 is the initial release of this application. The application works primarily as a visualization application
     *  pulling completed records from other application and also from the server
     *
     * This Application can be divided into modules which is
     * 1.) The Visualisation module which uses Android MP Chart library to render its statistical charts
     * 2.) The QR Code generator which uses the Zxing library for generating QR code for attendance purposes on meeting days
     *
     *
     * VISUALISATIONS
     * -------------------------
     * the visualization aspect of the application is built with 4 major filters namely 'Today','This week',  'This Payment Period', 'Since Year Started'
     *
     * Today
     * ------
     * this view is populated with data fed from other applications, Its is an entirely offline view meaning that the application does not need
     * data represented on this page for any of its analysis. However, the application relies on the records synced to the server from other
     * applications and have passed the criteria for 'i am working' and 'i am successful' for that application
     *
     *
     * This Week, This Payment Period, Since Year Started
     * ----------------------------------------------------------
     * This view is entirely dependent on the records for the  logged on user in the backend
     * this records are then downloaded into the application and then the 'This Week', 'This Payment Period' and 'Since Year Started' filters are applied when the user clicks on the Individual buttons accordingly
     *
     *
     * JAVA CLASSES IN VERSION 1.1.0
     * ---------------------------------
     *
     * Activity Class
     * ActivityDashboard
     * ActivityRecycler
     * FullDashboard
     * Main2Activity
     * MainActivity
     * Master
     * Note
     * NotesContentProvider
     * ProgressBarAnimation
     * QRCodeGenerator
     * SessionManagement
     * StatisticalData
     * SyncData
     * VisualizationPage
     * WorkingParams
     * YearActivityRecycler
     *
     *
     *
     *
     * **/


    //+++++++++++ VERSION 1.0.1 +++++++++++
    /**
     * String modifications were made to the application
     *
     *
     * */


}
