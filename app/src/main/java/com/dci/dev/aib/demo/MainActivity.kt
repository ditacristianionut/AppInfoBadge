package com.dci.dev.aib.demo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.dci.dev.appinfobadge.AppInfoBadge
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

		val fragment = AppInfoBadge
			.darkMode { false }
			.withAppIcon { true }
			.headerColor { resources.getColor(R.color.red_600)}
			.withPermissions { true }
			.withChangelog { true }
			.withLicenses { true }
			.withLibraries { true }
			.withRater { true }
			.withEmail { "dev.dci91@gmail.com" }
			.withSite { "https://github.com/ditacristianionut/AppInfoBadge" }
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
