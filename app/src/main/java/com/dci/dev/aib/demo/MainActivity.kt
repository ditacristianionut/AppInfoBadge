package com.dci.dev.aib.demo

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.dci.dev.appinfobadge.AppInfoBadge
import com.dci.dev.appinfobadge.InfoItemWithLink
import com.dci.dev.appinfobadge.InfoItemWithView
import com.dci.dev.appinfobadge.utils.dp
import com.dci.dev.appinfobadge.utils.px
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		val colors = arrayListOf<Int>(Color.RED, Color.BLACK, Color.BLUE, Color.GRAY, Color.GREEN)
		if (android.os.Build.VERSION.SDK_INT > 21) {
			val fields: Array<Field> =
				Class.forName("$packageName.R\$color").declaredFields
			for (field in fields) {
				val colorName: String = field.name
				val colorId: Int = field.getInt(null)
				val color = ResourcesCompat.getColor(resources, colorId, null)
				colors.add(color)
				Log.i("test", "$colorName => $colorId => $color")
			}
		}
		val randomDisplayBool = booleanArrayOf(true, false)

		val testItemWithView = InfoItemWithView(
			iconId = R.drawable.ic_test,
			title = "First custom item with view",
			headerColor = R.color.deep_purple_600,
			res = null,
			view = ImageView(this).also {
				it.setImageResource(R.drawable.ic_link)
				it.layoutParams = ViewGroup.LayoutParams(128.px, 128.px)
			}
		)

		val testItemWithRes = InfoItemWithView(
			iconId = R.drawable.ic_chemistry,
			title = "Second custom item with view",
			headerColor = R.color.indigo_600,
			res = R.layout.test_item,
			view = null
		)

		val testLinkItem = InfoItemWithLink(
			iconId = R.drawable.ic_link,
			title = "Custom link",
			headerColor = R.color.deep_purple_600,
			link = Uri.parse("http://www.google.com")
		)

		val fragment = AppInfoBadge
			.darkMode { true }
			.withAppIcon { true }
			.headerColor { resources.getColor(R.color.red_600)}
			.withPermissions { true }
			.withChangelog { true }
			.withLicenses { true }
			.withLibraries { true }
			.withRater { true }
			.withEmail { "dev.dci91@gmail.com" }
			.withSite { "https://github.com/ditacristianionut/AppInfoBadge" }
			.withCustomItems {
				listOf(testItemWithView, testItemWithRes, testLinkItem)
			}
			.show()


		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment, fragment)
			.commit()

		fab.setOnClickListener {
			val randomFragment = AppInfoBadge
				.darkMode { randomDisplayBool.random() }
				.withAppIcon { randomDisplayBool.random() }
				.headerColor { colors.random() }
				.withPermissions { randomDisplayBool.random() }
				.withChangelog { randomDisplayBool.random() }
				.withLicenses { randomDisplayBool.random() }
				.withLibraries { randomDisplayBool.random() }
				.withRater { randomDisplayBool.random() }
				.withEmail { "dev.dci91@gmail.com" }
				.withSite { "https://github.com/ditacristianionut/AppInfoBadge" }
				.show()
			supportFragmentManager.beginTransaction()
				.replace(R.id.fragment, randomFragment)
				.commit()
		}
	}
}
