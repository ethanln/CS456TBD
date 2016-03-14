package com.tbd.appprototype;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import model.InventoryItem;
import model.InventoryList;
import model.User;
import model.UsersAdapter;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;
import networking.callback.UserCallbackDelegate;
import networking.callback.UsersCallback;
import networking.callback.UsersCallbackDelegate;
import networking.testing.NetworkTest;
import networking.testing.NetworkTestAdapter;

public class NetworkTestActivity extends ListActivity{

    private ArrayList<NetworkTest> tests;
    private NetworkManager network;
    private NetworkTestActivity self;
    private Toast toast;
    private TBDApplication application;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        network = NetworkManager.getInstance();
        self = this;
        application = (TBDApplication) getApplication();
        users = new ArrayList<>();
        setUpList();
    }

    private void setUpList() {
        createTests();
        NetworkTestAdapter adapter = new NetworkTestAdapter(this, tests);
        setListAdapter(adapter);
        ListView listView = getListView();
        listView.setOnItemClickListener(onItemClickListener);

        final UsersCallback callback = new UsersCallback();
        UsersCallbackDelegate usersDelegate = new UsersCallbackDelegate(callback) {
            @Override
            public void makeCallback() {
                users = callback.getUsers();
                showNetworkTestCompleteToast("Users Loaded - Total: " + users.size());
            }
        };
        callback.setDelegate(usersDelegate);
        network.makeGetUsersAsListRequest(callback);
    }

    private void createTests() {
        this.tests = new ArrayList<>();

        // USER TESTS
        // ----------

        tests.add(new NetworkTest("Create New User Test") {
            @Override
            public void executeTest() {
                ArrayList<String> friends = new ArrayList<>();
                friends.add("TestFriendID0");
                friends.add("TestFriendID1");
                friends.add("TestFriendID2");
                User user = new User("Test", "test", "http://www.golenbock.com/wp-content/uploads/2015/01/placeholder-user.png", friends);
                network.makeCreateUserRequest(user, new GenericCallback() {
                    @Override
                    public void callback() {
                        showNetworkTestCompleteToast("UserID Created: " + this.data);
                    }
                });
            }
        });

        tests.add(new NetworkTest("Login User Test") {
            @Override
            public void executeTest() {
                network.makeLoginUserRequest("Test", "test", new GenericCallback() {
                    @Override
                    public void callback() {
                        TBDApplication app = (TBDApplication) getApplication();
                        User user = app.getCurrentUser();
                        if (user != null) {
                            showNetworkTestCompleteToast("Login User Done: " + user.getUserID());
                        } else {
                            showNetworkTestCompleteToast("Invalid Username / Password");
                        }
                    }
                });
            }
        });

        tests.add(new NetworkTest("Logout User Test") {
            @Override
            public void executeTest() {
                network.makeLogoutUserRequest(new GenericCallback() {
                    @Override
                    public void callback() {
                        showNetworkTestCompleteToast("Logout User Done");
                    }
                });
            }
        });

        tests.add(new NetworkTest("Get All Users (Adapter) Test") {
            @Override
            public void executeTest() {
                ArrayAdapter<User> testAdapter = new UsersAdapter(self, users);
                final UsersCallback callback = new UsersCallback();
                UsersCallbackDelegate usersDelegate = new UsersCallbackDelegate(callback) {
                    @Override
                    public void makeCallback() {
                        users = callback.getUsers();
                        showNetworkTestCompleteToast("Total Users: " + users.size());
                    }
                };
                callback.setDelegate(usersDelegate);
                network.makeGetUsersRequest(testAdapter, callback);
            }
        });

        tests.add(new NetworkTest("Get All Users (ArrayList) Test") {
            @Override
            public void executeTest() {
                final UsersCallback callback = new UsersCallback();
                UsersCallbackDelegate usersDelegate = new UsersCallbackDelegate(callback) {
                    @Override
                    public void makeCallback() {
                        users = callback.getUsers();
                        showNetworkTestCompleteToast("Total Users: " + users.size());
                    }
                };
                callback.setDelegate(usersDelegate);
                network.makeGetUsersAsListRequest(callback);
            }
        });

        tests.add(new NetworkTest("Get User By ID Test") {
            @Override
            public void executeTest() {
                if(users.size() > 0) {
                    final String userID = users.get(0).getUserID();
                    final UserCallback callback = new UserCallback();
                    UserCallbackDelegate userDelegate = new UserCallbackDelegate(callback) {
                        @Override
                        public void makeCallback() {
                            if (callback.getUser() == null) {
                                showNetworkTestCompleteToast("No User with ID: " + userID);
                            } else {
                                showNetworkTestCompleteToast("Got User: " + callback.getUser().getUsername());
                            }
                        }
                    };
                    callback.setDelegate(userDelegate);
                    network.makeGetUserRequest(userID, callback);
                } else {
                    showNetworkTestCompleteToast("No Users Available");
                }
            }
        });

        // Known Issue: If there are no users, the following code blows up. (Especially in the DB)
        tests.add(new NetworkTest("Update User") {
            @Override
            public void executeTest() {
                if(users.size() > 0) {
                    User user = users.get(0);
                    user.setUsername("Test Update User @" + new Date().getTime());
                    network.makeUpdateUserRequest(user, new GenericCallback() {
                        @Override
                        public void callback() {
                            if (error != null) {
                                showNetworkTestCompleteToast(error.getMessage());
                                Log.e("ERROR-MESSAGE", error.getMessage());
                                Log.e("ERROR-DETAILS", error.getDetails());
                            } else {
                                showNetworkTestCompleteToast("Update User Complete");
                            }
                        }
                    });
                } else {
                    showNetworkTestCompleteToast("No Users Available");
                }
            }
        });

        tests.add(new NetworkTest("Delete User - DANGEROUS") {
            @Override
            public void executeTest() {
                if (users.size() > 0) {
                    User user = users.get(0);
                    Log.d("IS USER NULL?", String.valueOf(user == null));
                    network.makeDeleteUserRequest(user.getUserID(), new GenericCallback() {
                        @Override
                        public void callback() {
                            if (this.error != null) {
                                showNetworkTestCompleteToast(this.error.getMessage());
                            } else {
                                showNetworkTestCompleteToast("Delete User Complete");
                            }
                        }
                    });
                } else {
                    showNetworkTestCompleteToast("No Users Available");
                }
            }
        });

        // LIST TESTS
        // ----------

        tests.add(new NetworkTest("Create New List Test") {
            @Override
            public void executeTest() {
                if (application.getCurrentUser() == null) {
                    showNetworkTestCompleteToast("No User Logged In");
                } else {
                    String userID = application.getCurrentUser().getUserID();
                    InventoryList list = new InventoryList(userID, "Test Title", "Test Type", "https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/512/film_reel.png");
                    network.makeCreateListRequest(list, new GenericCallback() {
                        @Override
                        public void callback() {
                            showNetworkTestCompleteToast("ListID Created: " + this.data + " - for User: " + application.getCurrentUser().getUsername());
                        }
                    });
                }
            }
        });

        // ITEM TESTS
        // ----------

        tests.add(new NetworkTest("Create New Item Test") {
            @Override
            public void executeTest() {
                InventoryItem item = new InventoryItem("", "Test Item", "Test Description", "https://pixabay.com/static/uploads/photo/2015/06/21/23/53/subtle-817155_960_720.jpg", "Test List ID");
                String itemID = network.makeCreateItemRequest(item);
                showNetworkTestCompleteToast("Item Created: " + itemID);
            }
        });


    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Index Tapped", String.valueOf(position));
            tests.get(position).executeTest();
        }
    };

    private void showNetworkTestCompleteToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
