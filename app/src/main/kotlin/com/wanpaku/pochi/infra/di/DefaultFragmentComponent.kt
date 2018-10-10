package com.wanpaku.pochi.infra.di

import com.wanpaku.pochi.app.booking.view.BookingListFragment
import com.wanpaku.pochi.app.dog_registration.DogRegistrationFormFragment
import com.wanpaku.pochi.app.launch.WelcomeFragment
import com.wanpaku.pochi.app.message.view.MessageFragment
import com.wanpaku.pochi.app.message.view.SitterMessageFragment
import com.wanpaku.pochi.app.message.view.UserMessageFragment
import com.wanpaku.pochi.app.search.view.ChangeZipCodeFragment
import com.wanpaku.pochi.app.search.view.SearchFragment
import com.wanpaku.pochi.app.search.view.SearchResultFragment
import com.wanpaku.pochi.app.sign_in.SignInFragment
import com.wanpaku.pochi.app.sign_up.SignUpFormFragment
import com.wanpaku.pochi.app.sitterdetail.SitterDetailFragment
import dagger.Subcomponent

@PerFragment
@Subcomponent()
interface DefaultFragmentComponent {
    fun inject(fragment: WelcomeFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: SignUpFormFragment)
    fun inject(fragment: DogRegistrationFormFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SitterDetailFragment)
    fun inject(fragment: SearchResultFragment)
    fun inject(fragment: ChangeZipCodeFragment)
    fun inject(fragment: BookingListFragment)
    fun inject(fragment: UserMessageFragment)
    fun inject(fragment: SitterMessageFragment)
}