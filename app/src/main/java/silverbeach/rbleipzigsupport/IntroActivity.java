package silverbeach.rbleipzigsupport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Match Center");
        sliderPage.setDescription("Verfolge die RB Leipzig Spiele im Match Center!");
        sliderPage.setImageDrawable(R.drawable.match);
        sliderPage.setBgColor(Color.parseColor("#6A0000"));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Transfergerüchte");
        sliderPage2.setDescription("Wer kommt? Wer geht? Sei immer auf dem neusten Stand!");
        sliderPage2.setImageDrawable(R.drawable.transfernews);
        sliderPage2.setBgColor(Color.parseColor("#6A0000"));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Beiträge");
        sliderPage3.setDescription("Diskutiere mit anderen RBL Fans über aktuelle Themen!");
        sliderPage3.setImageDrawable(R.drawable.posts);
        sliderPage3.setBgColor(Color.parseColor("#6A0000"));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Umfragen");
        sliderPage4.setDescription("Nimm bei spannenden RBL Umfragen teil und sieh die Meinung andere Fans!");
        sliderPage4.setImageDrawable(R.drawable.umfragen);
        sliderPage4.setBgColor(Color.parseColor("#6A0000"));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        SliderPage sliderPage5 = new SliderPage();
        sliderPage5.setTitle("Live Tabelle");
        sliderPage5.setDescription("Betrachte jederzeit die top-aktuelle Bundesligatabelle!");
        sliderPage5.setImageDrawable(R.drawable.tabelle);
        sliderPage5.setBgColor(Color.parseColor("#6A0000"));
        addSlide(AppIntroFragment.newInstance(sliderPage5));

        SliderPage sliderPage6 = new SliderPage();
        sliderPage6.setTitle("Fan Profil");
        sliderPage6.setDescription("Individualisiere dein Fan Profil");
        sliderPage6.setImageDrawable(R.drawable.profil);
        sliderPage6.setBgColor(Color.parseColor("#6A0000"));
        addSlide(AppIntroFragment.newInstance(sliderPage6));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#424242"));
        setSeparatorColor(Color.parseColor("#313131"));

        // Hide Skip/Done button.
        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent startIntent = new Intent(IntroActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}