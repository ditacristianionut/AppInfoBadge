package com.dci.dev.appinfobadge

import androidx.annotation.ColorInt


object AppInfoBadge {
    private var darkMode: Boolean = false
    private var withAppIcon: Boolean = true
    @ColorInt private var headerColor: Int = 0
    private var withPermissions: Boolean = true
    private var withChangelog: Boolean = true
    private var withEmail: String? = null
    private var withSite: String? = null
    private var withLicense: Boolean = true
    private var withLibraries: Boolean = true
    private var withRater: Boolean = true
    internal var customItems: List<BaseInfoItem> = listOf()

    /**
     * Switch between dark or light mode
     */
    fun darkMode(init: AppInfoBadge.() -> Boolean) = apply { darkMode = init() }

    /**
     * Show the icon of the app in the header view
     */
    fun withAppIcon(init: AppInfoBadge.() -> Boolean) = apply { withAppIcon = init() }

    /**
     * Header background color (resolved Color resource)
     */
    fun headerColor(init: AppInfoBadge.() ->  Int) = apply { headerColor = init() }

    /**
     * Show permissions item
     */
    fun withPermissions(init: AppInfoBadge.() -> Boolean) = apply { withPermissions = init() }

    /**
     * Show changelog item
     */
    fun withChangelog(init: AppInfoBadge.() -> Boolean) = apply { withChangelog = init() }

    /**
     * Set developer item
     */
    fun withEmail(init: AppInfoBadge.() -> String?) = apply { withEmail = init() }

    /**
     * Show developer  web page
     */
    fun withSite(init: AppInfoBadge.() -> String?) = apply { withSite = init() }

    /**
     * Show license item
     */
    fun withLicenses(init: AppInfoBadge.() -> Boolean) = apply { withLicense = init() }

    /**
     * Show libraries item
     */
    fun withLibraries(init: AppInfoBadge.() -> Boolean) = apply { withLibraries = init() }

    /**
     * Show app rating item
     */
    fun withRater(init: AppInfoBadge.() -> Boolean) = apply { withRater = init() }

    /**
     * Custom items
     */
    fun withCustomItems(init: AppInfoBadge.() -> List<BaseInfoItem>) = apply { customItems = init() }

    /**
     * Creates a new instance of [AppInfoBadgeFragment]
     */
    fun show(): AppInfoBadgeFragment = AppInfoBadgeFragment.newInstance(
        darkMode,
        withAppIcon,
        headerColor,
        withPermissions,
        withChangelog,
        withEmail,
        withSite,
        withLicense,
        withLibraries,
        withRater
    )

}
