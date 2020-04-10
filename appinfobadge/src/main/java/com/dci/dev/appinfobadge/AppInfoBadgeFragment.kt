package com.dci.dev.appinfobadge

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.WebView
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
import androidx.webkit.WebViewFeature
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.dci.dev.appinfobadge.permissions.PermissionItem
import com.dci.dev.appinfobadge.permissions.PermissionViewHolder
import com.dci.dev.appinfobadge.utils.*
import com.dci.dev.appinfobadge.view.BottomDialogContentView
import com.dci.dev.appinfobadge.view.DividerItemDecoration
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
                WITH_RATER to withRater
            )
        }
    }

    private val permissionsList = arrayListOf<PermissionItem>()

    private val defaultItems = arrayListOf<BaseInfoItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_info_badge, container, false)
    }

    fun bottomDialog(
        sheetPeek: Int,
        darkModeOn: Boolean,
        itemWithView: InfoItemWithView
    ): MaterialDialog {
        val view = BottomDialogContentView(context!!).also {
            it.setData(itemWithView)
            it.isDarkModeOn = darkModeOn
        }
        val dialog = MaterialDialog(context!!, BottomSheet())
            .customView(view = view, noVerticalPadding = true)
            .cornerRadius(20f)
            .setPeekHeight(sheetPeek)
            .lifecycleOwner(this@AppInfoBadgeFragment)
        return dialog
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
            val customItems =  AppInfoBadge.customItems
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
            val divider = DividerItemDecoration(
                ctx, DividerItemDecoration.VERTICAL, false, darkMode
            )
            header.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    header.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    sheetPeek = view.height - header.height
                }
            })
            val versionName = Utils.getAppVersionName(ctx)

            // App permissions
            if (withPermissions) {
                readPermissions(ctx)
                val rvPermissions = RecyclerView(context!!)
                rvPermissions.setBackgroundColor(bodyBackgroundColor)
                rvPermissions.setup {
                    val permissionsDataSource = dataSourceTypedOf(permissionsList)
                    withDataSource(permissionsDataSource)
                    withItem<PermissionItem, PermissionViewHolder>(R.layout.item_permission_view) {
                        onBind(::PermissionViewHolder) { index, item ->
                            settingsIv.imageTintList = iconTint
                            settingsIv.setOnClickListener {
                                ctx.goToSettings()
                            }
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
                defaultItems.add(
                    InfoItemWithView(
                        iconId = R.drawable.ic_banner_permissions_2,
                        title = getString(R.string.trust_badge),
                        headerColor = R.color.red_600,
                        res = null,
                        view = rvPermissions
                    )
                )
            }

            // Changelog
            if (withChangelog) {
                val webViewWhatsNew = WebView(ctx)
                webViewWhatsNew.settings.javaScriptEnabled = true
                if (darkMode && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    webViewWhatsNew.setBackgroundColor(Color.TRANSPARENT)
                }
                webViewWhatsNew.loadAsset("changelog.html", context!!.applicationContext, darkMode)
                defaultItems.add(
                    InfoItemWithView(
                        iconId = R.drawable.ic_banner_whats_new,
                        title = getString(R.string.what_s_new),
                        headerColor = R.color.green_600,
                        res = null,
                        view = webViewWhatsNew
                    )
                )
            }

            // License
            if (withLicense) {
                val webViewLicense = WebView(ctx)
                webViewLicense.settings.javaScriptEnabled = true
                if (darkMode && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    webViewLicense.setBackgroundColor(Color.TRANSPARENT)
                }
                webViewLicense.loadAsset("license.html", context!!.applicationContext, darkMode)
                defaultItems.add(
                    InfoItemWithView(
                        iconId = R.drawable.ic_banner_licenses,
                        title = getString(R.string.license),
                        headerColor = R.color.orange_600,
                        res = null,
                        view = webViewLicense
                    )
                )
            }

            // Libraries
            if (withLibraries) {
                val webViewLibraries = WebView(ctx)
                webViewLibraries.settings.javaScriptEnabled = true
                if (darkMode && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    webViewLibraries.setBackgroundColor(Color.TRANSPARENT)
                }
                webViewLibraries.loadAsset("libraries.html", context!!.applicationContext, darkMode)
                defaultItems.add(
                    InfoItemWithView(
                        iconId = R.drawable.ic_banner_libraries,
                        title = getString(R.string.libraries),
                        headerColor = R.color.grey_600,
                        res = null,
                        view = webViewLibraries
                    )
                )
            }

            // Contact
            if (!withEmail.isNullOrBlank() || !withSite.isNullOrBlank()) {
                val contactContentView = LayoutInflater.from(context)
                    .inflate(R.layout.custom_view_contact, null, false)
                val containerMail =
                    contactContentView.findViewById<LinearLayout>(R.id.containerMail)
                val containerSite = contactContentView
                    .findViewById<LinearLayout>(R.id.containerSite)
                val mail = contactContentView
                    .findViewById<TextView>(R.id.tvMail)
                val site = contactContentView
                    .findViewById<TextView>(R.id.tvSite)
                val iconMail = contactContentView
                    .findViewById<ImageView>(R.id.ivMail)
                val iconSite = contactContentView
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
                    defaultItems.add(
                        InfoItemWithView(
                            iconId = R.drawable.ic_banner_contact,
                            title = getString(R.string.contact),
                            headerColor = R.color.orange_600,
                            res = null,
                            view = contactContentView
                        )
                    )
                }
            }

            // Rater
            if (withRater) {
                defaultItems.add(
                    InfoItemWithLink(
                        iconId = R.drawable.ic_banner_rate,
                        title = getString(R.string.rate_me),
                        headerColor = R.color.red_600,
                        link = Uri.parse("market://details?id=" + ctx.packageName)
                    )
                )
            }

            defaultItems.addAll(customItems)

            // Header
            header.setBackgroundColor(headerColor)
            if (withAppIcon) {
                ivAppIcon.setImageDrawable(appIcon)
            } else {
                ivAppIcon.visibility = View.INVISIBLE
            }

            // Body
            main?.setBackgroundColor(bodyBackgroundColor)
            val dataSource = dataSourceTypedOf(
                defaultItems
            )
            rvItems.setup {
                val emptyMessage = TextView(context!!)
                emptyMessage.text = "No item"
                withEmptyView(emptyMessage)
                withDataSource(dataSource)
                withItem<InfoItemWithView, InfoItemViewHolder>(R.layout.info_item) {
                    onBind(::InfoItemViewHolder) { index: Int, itemWithView: InfoItemWithView ->
                        title.setTextColor(bodyTextColor)
                        if (itemWithView !in customItems)
                            icon.imageTintList = iconTint
                        title.text = itemWithView.title
                        icon.setImageResource(itemWithView.iconId)
                    }
                    onClick { index: Int ->
                        bottomDialog(
                            sheetPeek,
                            darkMode,
                            item
                        ).show()
                    }
                }
                withItem<InfoItemWithLink, InfoItemViewHolder>(R.layout.info_item) {
                    onBind(::InfoItemViewHolder) { index: Int, itemWithLink: InfoItemWithLink ->
                        title.setTextColor(bodyTextColor)
                        if (itemWithLink !in customItems)
                            icon.imageTintList = iconTint
                        title.text = itemWithLink.title
                        icon.setImageResource(itemWithLink.iconId)
                    }
                    onClick { index: Int ->
                        Utils.openUri(ctx, item.link)
                    }
                }
            }
            rvItems.addItemDecoration(divider)

            // App name & version
            tvAppName.text = ctx.applicationInfo?.loadLabel(context!!.packageManager)
            if (versionName.isBlank()) {
                tvAppVersion.isGone = true
            } else {
                tvAppVersion.text = getString(R.string.app_version_name, versionName)
            }
            tvAppName.setTextColor(headerTextColor)
            tvAppVersion.setTextColor(headerTextColor)

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
