package com.dotjoo.aghsilinicustomer.ui.fragment.auth.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.Param.AddAddressParam
import com.dotjoo.aghsilinicustomer.data.Param.AddAddressParams
import com.dotjoo.aghsilinicustomer.databinding.BotomMapBinding
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderViewModel
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAddressAddesClickLisener
import com.dotjoo.aghsilinicustomer.util.MapUtil
import com.dotjoo.aghsilinicustomer.util.PermissionManager
import com.dotjoo.aghsilinicustomer.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinicustomer.util.WWLocationManager
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.isNull
import com.dotjoo.aghsilinicustomer.util.observe
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.botom_map.*
 import javax.inject.Inject

interface onLocationClick {
    fun onClick( lat :Double?, long :Double?,address : AddAddressParams?)
}

@AndroidEntryPoint
class MapBottomSheet(var onClick: onLocationClick?, val  onFinish: OnAddressAddesClickLisener?) :     DialogFragment(R.layout.botom_map)
   , OnMapReadyCallback {
    private   val TAG = "MapBottomSheet"
    val mViewModel: CreateOrderViewModel by activityViewModels()

    private var lat: Double?= null
    private var long: Double?= null
    private lateinit var binding: BotomMapBinding

    private var loc: Location? = null
    private var address: AddAddressParams? = null
    @Inject   lateinit var  app: Application

     companion object {
        fun newInstance(onClick: onLocationClick?, onFinish: OnAddressAddesClickLisener?): MapBottomSheet {
            val args = Bundle()
            val f = MapBottomSheet(onClick,onFinish)
            f.arguments = args
            return f
        }
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var selectedLocation: LatLng
    private var marker: Marker? = null

    @Inject
    lateinit var locationManager: WWLocationManager

    @Inject
    lateinit var permissionManager: PermissionManager


    private fun getLocationNow() {
        locationManager.getLastKnownLocation { location ->
            loc = location
            loc?.latitude?.let { loc?.longitude?.let { it1 -> showlocation(it, it1, true) } }

        }
    }
    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BotomMapBinding.inflate(inflater)
        Places.initialize(activity?.applicationContext,getString(R.string.google_maps_key))

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(activity)
        onClick()
        mViewModel.apply {

            observe(viewState) {
                handleViewState(it)
            }

        }
        return binding.root
    }

    private fun showlocation(latitude: Double, longitude: Double, b: Boolean) {
        lat=latitude
        long= longitude
        address= locationManager.getAddress(latitude, longitude)
        binding?.tvAddress?.text = address?.fullAddress
        if (b)   onMapReady(googleMap, latitude, longitude)
    }

    private fun onMapReady(gm: GoogleMap, latitude:Double, longitude:Double) {
        MapUtil.disableMarkerClick(gm, true)
        val pos = LatLng(latitude, longitude)
        setMarker(gm, pos)
        MapUtil.moveCameraAt(gm, pos)
     }
    @Nullable
    @Override
    public fun getChildFragmentManager( fragment: Fragment): FragmentManager {
        return fragment.getChildFragmentManager();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult: $data")

    }

    private fun setMarker(gm: GoogleMap, position: LatLng) {
        if (marker != null) {
            marker?.remove()
        }
        marker = MapUtil.addMarker(requireContext(), gm, position, "", R.drawable.ic_location)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val location = CameraUpdateFactory.newLatLngZoom(selectedLocation, 12f)

        this.googleMap.uiSettings.isZoomControlsEnabled = true
        this.googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        this.googleMap.uiSettings.setAllGesturesEnabled(true)
        this.googleMap.uiSettings.isMyLocationButtonEnabled = true
         this.googleMap.animateCamera(location)
        this.googleMap.getUiSettings().setIndoorLevelPickerEnabled(false)
          getLocationNow()
         this.googleMap.setOnCameraIdleListener {
            selectedLocation = LatLng(
                this.googleMap.cameraPosition.target.latitude,
                this.googleMap.cameraPosition.target.longitude
            )


                showlocation(
                    googleMap.cameraPosition.target.latitude,
                    googleMap.cameraPosition.target.longitude,
                    false
                )
            }


        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
      /*  val success =this. googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.style_json)
        )*/

        /*   binding.swiperefreshHome.setOnRefreshListener {
                   mViewModel.getServices()
                   if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
               }*/
    }


    fun handleViewState(action: CreateOrderAction) {
        when (action) {
            is CreateOrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is CreateOrderAction.ShowFailureMsg -> {
                binding.btnDone.isEnabled= true

                action.message?.let {
                    if (it.contains("401") == true) {
                        findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                    } else {
                        showToast(requireContext(), action.message.toString())
                        showProgress(false)
                    }
                }
            }

            is CreateOrderAction.AddressAdded -> {
 onFinish?.onAddressClickLisener()
                    dismiss()

            }


            else -> {

            }
        }
    }

    private fun showProgress(show: Boolean) {

    }

    private fun onClick() {
        binding?.locationCard?.setOnClickListener {
         }
        binding?.btnDone?.setOnClickListener {
            if(onClick?.isNull() == true || onClick==null){
address?.fullAddress?.let { it1 ->
     mViewModel.addAddress(  AddAddressParam(
        lat.toString(),
        long.toString(),
        it1
    )
)
            }

            }else{
                onClick?.onClick(lat, long, address)
                dismiss()
            }

        }
    }


    override fun onResume() {
        super.onResume()
        binding?.map?.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding?.map?.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding?.map?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.map?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.map?.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window?.setGravity(Gravity.CENTER)


        binding?.map?.onCreate(savedInstanceState)
        binding?.map?.onResume();
        //    binding.map.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding?.map?.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            // For showing a move to my location button
            if (activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED) {
            }
            mMap.setMyLocationEnabled(true)

            this.googleMap.uiSettings.isZoomControlsEnabled = true
            this.googleMap.uiSettings.isIndoorLevelPickerEnabled = true
            this.googleMap.uiSettings.setAllGesturesEnabled(true)
            this.googleMap.uiSettings.isMyLocationButtonEnabled = true
            this.googleMap.getUiSettings().setIndoorLevelPickerEnabled(false)
            this.googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL);
            googleMap?.isMyLocationEnabled = true
             getLocationNow()
            googleMap.setOnMapClickListener {
                showlocation(it.latitude, it.longitude, true)
            }

        })

        onClick()
        if (!Places.isInitialized()) {
            Places.initialize(requireContext().getApplicationContext(), getString(R.string.google_maps_key));
        }
         val autocompleteFragment =
            getChildFragmentManager(this).findFragmentById(R.id.autocomtmpletee_fragment)as AutocompleteSupportFragment?

        var placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        autocompleteFragment?.setPlaceFields(placeFields)

         autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
           try{
               showlocation(place.latLng.latitude, place.latLng.longitude, true)
           }catch (e:Exception){

           }
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }
}

