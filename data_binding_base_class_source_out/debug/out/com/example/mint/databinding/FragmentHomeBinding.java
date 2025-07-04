// Generated by view binder compiler. Do not edit!
package com.example.mint.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.mint.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final CardView budgetCv;

  @NonNull
  public final LinearLayout contentLl;

  @NonNull
  public final LinearLayout contenttLl;

  @NonNull
  public final TextView expensesBalanceTv;

  @NonNull
  public final CardView expensesCv;

  @NonNull
  public final TextView incomeBalanceTv;

  @NonNull
  public final LinearLayout mainLl;

  @NonNull
  public final TextView usernameDisplayTv;

  private FragmentHomeBinding(@NonNull FrameLayout rootView, @NonNull CardView budgetCv,
      @NonNull LinearLayout contentLl, @NonNull LinearLayout contenttLl,
      @NonNull TextView expensesBalanceTv, @NonNull CardView expensesCv,
      @NonNull TextView incomeBalanceTv, @NonNull LinearLayout mainLl,
      @NonNull TextView usernameDisplayTv) {
    this.rootView = rootView;
    this.budgetCv = budgetCv;
    this.contentLl = contentLl;
    this.contenttLl = contenttLl;
    this.expensesBalanceTv = expensesBalanceTv;
    this.expensesCv = expensesCv;
    this.incomeBalanceTv = incomeBalanceTv;
    this.mainLl = mainLl;
    this.usernameDisplayTv = usernameDisplayTv;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.budgetCv;
      CardView budgetCv = ViewBindings.findChildViewById(rootView, id);
      if (budgetCv == null) {
        break missingId;
      }

      id = R.id.contentLl;
      LinearLayout contentLl = ViewBindings.findChildViewById(rootView, id);
      if (contentLl == null) {
        break missingId;
      }

      id = R.id.contenttLl;
      LinearLayout contenttLl = ViewBindings.findChildViewById(rootView, id);
      if (contenttLl == null) {
        break missingId;
      }

      id = R.id.expensesBalanceTv;
      TextView expensesBalanceTv = ViewBindings.findChildViewById(rootView, id);
      if (expensesBalanceTv == null) {
        break missingId;
      }

      id = R.id.expensesCv;
      CardView expensesCv = ViewBindings.findChildViewById(rootView, id);
      if (expensesCv == null) {
        break missingId;
      }

      id = R.id.incomeBalanceTv;
      TextView incomeBalanceTv = ViewBindings.findChildViewById(rootView, id);
      if (incomeBalanceTv == null) {
        break missingId;
      }

      id = R.id.mainLl;
      LinearLayout mainLl = ViewBindings.findChildViewById(rootView, id);
      if (mainLl == null) {
        break missingId;
      }

      id = R.id.usernameDisplayTv;
      TextView usernameDisplayTv = ViewBindings.findChildViewById(rootView, id);
      if (usernameDisplayTv == null) {
        break missingId;
      }

      return new FragmentHomeBinding((FrameLayout) rootView, budgetCv, contentLl, contenttLl,
          expensesBalanceTv, expensesCv, incomeBalanceTv, mainLl, usernameDisplayTv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
