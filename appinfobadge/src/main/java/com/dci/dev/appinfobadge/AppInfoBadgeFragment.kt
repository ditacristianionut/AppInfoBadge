package com.dci.dev.appinfobadge

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.dci.dev.appinfobadge.permissions.PermissionItem
import com.dci.dev.appinfobadge.permissions.PermissionViewHolder
import com.dci.dev.appinfobadge.utils.*
import com.dci.dev.appinfobadge.view.RoundedCornerImageView
import dev.jorgecastillo.androidcolorx.library.isDark
import kotlinx.android.synthetic.main.fragment_app_info_badge.*
import kotlin.math.roundToInt


class AppInfoBadgeFragment : Fragment() {

    companion object {
        private const val DARK_MODE = "dark_mode"
        private const val WITH_APP_ICON = "with_app_icon"
        private const val HEADER_COLOR = "header_color"
        private const val WITH_CHANGELOG = "with_changelog"
        private const val WITH_PERMISSIONS = "with_permissions"
        private const val WITH_EMAIL= "with_email"
        private const val WITH_SITE = "with_site"
        private const val WITH_LICENSE = "with_license"
        private const val WITH_LIBRARIES = "with_libraries"
        private const val WITH_RATER = "with_rater"

        fun newInstance(
            darkMode: Boolean,
            withAppIcon: Boolean,
            headerColor: Int,
            withPermissions: Boolean,
            withChangelog: Boolean,
            withEmail: String?,
            withSite: String?,
            withLicense: Boolean,
            withLibraries: Boolean,
            withRater: Boolean) = AppInfoBadgeFragment().apply {
            arguments = bundleOf(
                DARK_MODE to darkMode,
                WITH_APP_ICON to withAppIcon,
                HEADER_COLOR to headerColor,
                WITH_PERMISSIONS to withPermissions,
                WITH_CHANGELOG to withChangelog,
                WITH_EMAIL to withEmail,
                WITH_SITE to withSite,
                WITH_LICENSE to withLicense,
                WITH_LIBRARIES to withLibraries,
                WITH_RATER to withRater)
        }
    }

    private val permissionsList = arrayListOf<PermissionItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_info_badge, container, false)
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { ctx ->
            val darkMode = arguments?.getBoolean(DARK_MODE) ?: DefaultValues.darkMode
            val withAppIcon = arguments?.getBoolean(WITH_APP_ICON) ?: DefaultValues.withAppIcon
            @ColorInt val headerColor = arguments?.getInt(HEADER_COLOR) ?: DefaultValues.headerColor(ctx)
            val withPermissions = arguments?.getBoolean(WITH_PERMISSIONS) ?: DefaultValues.withPermissions
            val withChangelog = arguments?.getBoolean(WITH_CHANGELOG) ?: DefaultValues.withChangelog
            val withLicense = arguments?.getBoolean(WITH_LICENSE) ?: DefaultValues.withLicense
            val withLibraries = arguments?.getBoolean(WITH_LIBRARIES) ?: DefaultValues.withLibraries
            val withRater = arguments?.getBoolean(WITH_RATER) ?: DefaultValues.withRater
            val withEmail = arguments?.getString(WITH_EMAIL) ?: DefaultValues.withEmail
            val withSite = arguments?.getString(WITH_SITE) ?: DefaultValues.withSite
            val headerTextColor = if (headerColor.isDark())
                ctx.resolveColor(R.color.grey_100)
            else
                ctx.resolveColor(R.color.grey_900)
            val appIcon = ctx.resolveDrawable(ctx.applicationInfo.icon)
            var sheetPeek = (ctx.displayHeight * 0.6).roundToInt()
            val bodyBackgroundColor = if (darkMode)
                ctx.resolveColor(R.color.dark_background)
            else
                ctx.resolveColor(R.color.light_background)
            val bodyTextColor = if (darkMode)
                ctx.resolveColor(R.color.grey_100)
            else
                ctx.resolveColor(R.color.grey_900)
            val iconTint = ColorStateList.valueOf(bodyTextColor)
            header.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    header.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    sheetPeek = view.height - header.height
                }
            })
            val versionName = Utils.getAppVersionName(ctx)

            // Header
            header.setBackgroundColor(headerColor)
            if (withAppIcon) {
                ivAppIcon.setImageDrawable(appIcon)
            } else {
                ivAppIcon.visibility = View.INVISIBLE
            }

            // Body
            main.setBackgroundColor(bodyBackgroundColor)

            // App name & version
            tvAppName.text = ctx.applicationInfo?.loadLabel(context!!.packageManager)
            if (versionName.isBlank()) {
                tvAppVersion.isGone = true
            } else {
                tvAppVersion.text = getString(R.string.app_version_name, versionName)
            }
            tvAppName.setTextColor(headerTextColor)
            tvAppVersion.setTextColor(headerTextColor)

            // App permissions
            containerAppPermissions.isVisible = withPermissions
            if (withPermissions) {
                readPermissions(ctx)
                val permissionsDataSource = dataSourceTypedOf(permissionsList)
                ivAppPermissions.imageTintList = iconTint
                tvAppPermissions.setTextColor(bodyTextColor)
                tvAppPermissions.setOnClickListener {
                    val dialog = MaterialDialog(context!!, BottomSheet()).show {
                        customView(R.layout.custom_view_permissions, noVerticalPadding = true)
                        cornerRadius(20f)
                        setPeekHeight(sheetPeek)
                        lifecycleOwner(this@AppInfoBadgeFragment)
                    }
                    dialog.onShow {
                        val banner = it.getCustomView()
                            .findViewById<RoundedCornerImageView>(R.id.ivBannerBackground)
                        banner.radius = RoundedCornerImageView.Radius(20.px, 20.px, 0.px, 0.px)
                        banner.roundedCorners = RoundedCornerImageView.CORNER_ALL
                        val recyclerView = it.getCustomView()
                            .findViewById<RecyclerView>(R.id.rvPermissions)
                        recyclerView.setBackgroundColor(bodyBackgroundColor)
                        recyclerView.setup {
                            withDataSource(permissionsDataSource)
                            withItem<PermissionItem, PermissionViewHolder>(R.layout.item_permission_view) {
                                onBind(::PermissionViewHolder) { index, item ->
                                    name.text = item.name
                                    val textColor = if (item.isGranted)
                                        ctx.resolveColor(R.color.green_400)
                                    else
                                        ctx.resolveColor(R.color.red_400)
                                    name.setTextColor(textColor)
                                    try {
                                        description.text = getString(item.description)
                                        description.setTextColor(bodyTextColor)
                                    } catch (exception: Resources.NotFoundException) {
                                        Log.e(
                                            "AIB",
                                            "Permission details not found for [${item.name}]"
                                        )
                                    }
                                    settings.setOnClickListener {
                                        ctx.goToSettings()
                                    }
                                }
                                onClick { index ->
                                    // item is a `val` in `this` here
                                }
                                onLongClick { index ->
                                    // item is a `val` in `this` here
                                }
                            }
                        }
                    }
                }
            }

            // Changelog
            containerChangelog.isVisible = withChangelog
            if (withChangelog) {
                ivAppChangelog.imageTintList = iconTint
                tvAppChangelog.setTextColor(bodyTextColor)
                tvAppChangelog.setOnClickListener {
                    val dialog = MaterialDialog(context!!, BottomSheet()).show {
                        customView(R.layout.custom_view_changelog, noVerticalPadding = true)
                        cornerRadius(20f)
                        setPeekHeight(sheetPeek)
                        lifecycleOwner(this@AppInfoBadgeFragment)
                    }
                    dialog.onShow {
                        val banner = it.getCustomView()
                            .findViewById<RoundedCornerImageView>(R.id.ivBannerBackground)
                        banner.radius = RoundedCornerImageView.Radius(20.px, 20.px, 0.px, 0.px)
                        banner.roundedCorners = RoundedCornerImageView.CORNER_ALL
                        val webView: WebView = it.getCustomView()
                            .findViewById<WebView>(R.id.web_view)
                        webView.settings.javaScriptEnabled = true
                        if (darkMode && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                            WebSettingsCompat.setForceDark(
                                webView.settings,
                                WebSettingsCompat.FORCE_DARK_ON
                            )
                        } else {
                            webView.setBackgroundColor(Color.TRANSPARENT)
                        }
                        webView.loadAsset("changelog.html", context!!.applicationContext, darkMode)
                        val container = it.getCustomView()
                            .findViewById<FrameLayout>(R.id.container)
                        container.setBackgroundColor(bodyBackgroundColor)
                    }
                }
            }

            // License
            containerLicense.isVisible = withLicense
            if (withLicense) {
                ivAppLicense.imageTintList = iconTint
                tvAppLicense.setTextColor(bodyTextColor)
                tvAppLicense.setOnClickListener {
                    val dialog = MaterialDialog(ctx, BottomSheet()).show {
                        customView(R.layout.custom_view_license, noVerticalPadding = true)
                        cornerRadius(20f)
                        setPeekHeight(sheetPeek)
                        lifecycleOwner(this@AppInfoBadgeFragment)
                    }
                    dialog.onShow {
                        val banner = it.getCustomView()
                            .findViewById<RoundedCornerImageView>(R.id.ivBannerBackground)
                        banner.radius = RoundedCornerImageView.Radius(20.px, 20.px, 0.px, 0.px)
                        banner.roundedCorners = RoundedCornerImageView.CORNER_ALL
                        val webView: WebView = it.getCustomView()
                            .findViewById<WebView>(R.id.web_view)
                        webView.settings.javaScriptEnabled = true
                        if (darkMode && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                            WebSettingsCompat.setForceDark(
                                webView.settings,
                                WebSettingsCompat.FORCE_DARK_ON
                            )
                        } else {
                            webView.setBackgroundColor(Color.TRANSPARENT)
                        }
                        webView.loadAsset("license.html", context!!.applicationContext, darkMode)
                        val container = it.getCustomView()
                            .findViewById<FrameLayout>(R.id.container)
                        container.setBackgroundColor(bodyBackgroundColor)
                    }
                }
            }

            // Libraries
            containerLibraries.isVisible = withLibraries
            if (withLibraries) {
                ivAppLibraries.imageTintList = iconTint
                tvAppLibraries.setTextColor(bodyTextColor)
                tvAppLibraries.setOnClickListener {
                    val dialog = MaterialDialog(ctx, BottomSheet()).show {
                        customView(R.layout.custom_view_libraries, noVerticalPadding = true)
                        cornerRadius(20f)
                        setPeekHeight(sheetPeek)
                        lifecycleOwner(this@AppInfoBadgeFragment)
                    }
                    dialog.onShow {
                        val banner = it.getCustomView()
                            .findViewById<RoundedCornerImageView>(R.id.ivBannerBackground)
                        banner.radius = RoundedCornerImageView.Radius(20.px, 20.px, 0.px, 0.px)
                        banner.roundedCorners = RoundedCornerImageView.CORNER_ALL
                        val webView: WebView = it.getCustomView()
                            .findViewById<WebView>(R.id.web_view)
                        webView.settings.javaScriptEnabled = true
                        if (darkMode && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                            WebSettingsCompat.setForceDark(
                                webView.settings,
                                WebSettingsCompat.FORCE_DARK_ON
                            )
                        } else {
                            webView.setBackgroundColor(Color.TRANSPARENT)
                        }
                        webView.loadAsset("libraries.html", ctx.applicationContext, darkMode)
                        val container = it.getCustomView()
                            .findViewById<FrameLayout>(R.id.container)
                        container.setBackgroundColor(bodyBackgroundColor)
                    }
                }
            }

            // App Rate
            containerRater.isVisible = withRater
            if (withRater) {
                ivAppRate.imageTintList = iconTint
                tvAppRate.setTextColor(bodyTextColor)
                tvAppRate.setOnClickListener {
                    ctx.let {
                        Utils.openAppInPlayStore(it)
                    }
                }
            }

            // Contact
            containerContact.isVisible = !withEmail.isNullOrBlank() || !withSite.isNullOrBlank()
            ivContact.imageTintList = iconTint
            tvContact.setTextColor(bodyTextColor)
            tvContact.setOnClickListener {
                val dialog = MaterialDialog(context!!, BottomSheet()).show {
                    customView(R.layout.custom_view_contact, noVerticalPadding = true)
                    cornerRadius(20f)
                    setPeekHeight(sheetPeek)
                    lifecycleOwner(this@AppInfoBadgeFragment)
                }
                dialog.onShow {
                    val banner = it.getCustomView()
                        .findViewById<RoundedCornerImageView>(R.id.ivBannerBackground)
                    banner.radius = RoundedCornerImageView.Radius(20.px, 20.px, 0.px, 0.px)
                    banner.roundedCorners = RoundedCornerImageView.CORNER_ALL
                    val content = it.getCustomView()
                        .findViewById<LinearLayout>(R.id.content)
                    content.setBackgroundColor(bodyBackgroundColor)
                    val containerMail = it.getCustomView()
                        .findViewById<LinearLayout>(R.id.containerMail)
                    val containerSite = it.getCustomView()
                        .findViewById<LinearLayout>(R.id.containerSite)
                    val mail = it.getCustomView()
                        .findViewById<TextView>(R.id.tvMail)
                    val site = it.getCustomView()
                        .findViewById<TextView>(R.id.tvSite)
                    val iconMail = it.getCustomView()
                        .findViewById<ImageView>(R.id.ivMail)
                    val iconSite = it.getCustomView()
                        .findViewById<ImageView>(R.id.ivSite)

                    containerMail.isVisible = !withEmail.isNullOrBlank()
                    if (!withEmail.isNullOrBlank()) {
                        mail.text = withEmail
                        iconMail.imageTintList = iconTint
                    }

                    containerSite.isVisible = !withSite.isNullOrBlank()
                    if (!withSite.isNullOrBlank()) {
                        site.text = withSite
                        when {
                            withSite.contains("github", true) -> {
                                iconSite.setImageResource(R.drawable.ic_contact_site_github)
                            }
                            withSite.contains("gitlab", true) -> {
                                iconSite.setImageResource(R.drawable.ic_contact_site_gitlab)
                            }
                            withSite.contains("bitbucket", true) -> {
                                iconSite.setImageResource(R.drawable.ic_contact_site_bitbucket)
                            }
                            withSite.contains("facebook", true) -> {
                                iconSite.setImageResource(R.drawable.ic_contact_site_facebook)
                            }
                        }
                        iconSite.imageTintList = iconTint
                    }
                }
            }
        }
    }

    private fun readPermissions(context: Context) {
        permissionsList.clear()
        val appPermissions = PermissionUtils.retrievePermissions(context).map {
            PermissionUtils.getPermissionName(it, context)?.let { _name ->
                PermissionItem(
                    name = _name,
                    description = PermissionUtils.getPermissionDescription(it, context) ?: 0,
                    isGranted = PermissionUtils.hasPermission(it, context)
                )
            }
        }
        appPermissions.forEach { it?.let { permissionsList.add(it) } }
    }

    private object DefaultValues {
        fun headerColor(context: Context) = ResourcesCompat.getColor(context.resources, R.color.green_400, null)
        const val darkMode = false
        const val withPermissions = true
        const val withChangelog = true
        const val withLicense = true
        const val withLibraries = true
        const val withRater = true
        const val withAppIcon = true
        val withEmail: String? = null
        val withSite: String? = null
    }
}
