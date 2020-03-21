package com.orange.ols.appinfobadge

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

		val fragment = AppInfoBadge.withChangelog { true }
			.headerColor { resources.getColor(R.color.blue_900)}
			.withPermissions { true }
			.withEmail { "dita.cristian.ionut@gmail.com" }
			.withSite { "https://www.abc.com" }
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
				.withRater { randomDisplayBool.random() }
				.withEmail { "dita.cristian.ionut@gmail.com" }
				.withSite { "https://www.abc.com" }
				.show()
			supportFragmentManager.beginTransaction()
				.replace(R.id.fragment, randomFragment)
				.commit()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			R.id.action_settings -> true
			else -> super.onOptionsItemSelected(item)
		}
	}
}
