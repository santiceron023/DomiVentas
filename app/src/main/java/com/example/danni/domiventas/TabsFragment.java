package com.example.danni.domiventas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabsFragment extends Fragment {
    private AppBarLayout appBar;
    private TabLayout tabs;
    private android.support.v4.view.ViewPager ViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabs2, container, false);

        View contenedor= (View)container.getParent();
        appBar = (AppBarLayout)contenedor.findViewById(R.id.appbar);
        tabs= new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#FF9800"));
        appBar.addView(tabs);

        ViewPager=(android.support.v4.view.ViewPager)view.findViewById(R.id.pagerTabs);
        TabsFragment.ViewPagerAdapter pagerAdapater= new TabsFragment.ViewPagerAdapter(getFragmentManager());
        ViewPager.setAdapter(pagerAdapater);
        tabs.setupWithViewPager(ViewPager);

        return view;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        String[] tituloTabs={"Pedidos", "Productos","Promociones"};

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0: return new PedidosFragment();
                case 1: return new ProductosFragment();
                case 2: return new PromocionesFragment();

            }
            return null;
        }
        @Override
        public int getCount(){
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position){
            return tituloTabs[position];
        }

    }

}
