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
import java.util.HashMap;

import model.InventoryItem;
import model.InventoryList;
import model.User;
import model.UsersAdapter;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ListCallback;
import networking.callback.ListCallbackDelegate;
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
    private ArrayList<InventoryList> lists;
    private ArrayList<String> friendIDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        network = NetworkManager.getInstance();
        self = this;
        application = (TBDApplication) getApplication();
        users = new ArrayList<>();
        lists = new ArrayList<>();
        friendIDs = new ArrayList<>();
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
                HashMap<String, Object> friends = new HashMap<>();
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

        // FRIENDS TESTS
        // -------------

        tests.add(new NetworkTest("Get Friend IDs Test") {
            @Override
            public void executeTest() {
                friendIDs = new ArrayList<>();
                network.makeGetFriendsRequest(friendIDs, new GenericCallback() {
                    @Override
                    public void callback() {
                        showNetworkTestCompleteToast(data);
                    }
                });
            }
        });

        tests.add(new NetworkTest("Add Friend Test") {
            @Override
            public void executeTest() {
                String testFriendID = "TestFriendID" + new Date().getTime();
                network.makeAddFriendRequest(application.getCurrentUserID(), testFriendID, new GenericCallback() {
                    @Override
                    public void callback() {
                        showNetworkTestCompleteToast("Friend Created: " + data);
                    }
                });
            }
        });

        tests.add(new NetworkTest("Remove Friend Test") {
            @Override
            public void executeTest() {
                if (friendIDs.size() > 0) {
                    final String friendID = friendIDs.get(0);
                    network.makeRemoveFriendRequest(friendID, new GenericCallback() {
                        @Override
                        public void callback() {
                            if (error == null) {
                                showNetworkTestCompleteToast("Removed Friend ID: " + friendID);
                            } else {
                                showNetworkTestCompleteToast(error.getMessage());
                            }
                        }
                    });
                } else {
                    showNetworkTestCompleteToast("No Friend IDs available");
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

        tests.add(new NetworkTest("Get All Lists Test For Logged In User") {
            @Override
            public void executeTest() {
                if (application.getCurrentUser() == null) {
                    showNetworkTestCompleteToast("No User Logged In");
                } else {
                    ArrayAdapter<InventoryList> adapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, lists);
                    network.makeGetListsRequest(adapter, new GenericCallback() {
                        @Override
                        public void callback() {
                            showNetworkTestCompleteToast(this.data);
                        }
                    });
                }
            }
        });

        tests.add(new NetworkTest("Get List By ID") {
            @Override
            public void executeTest() {
                if (lists.size() > 0) {
                    final String listID = lists.get(0).getListID();
                    final ListCallback callback = new ListCallback();
                    ListCallbackDelegate listDelegate = new ListCallbackDelegate(callback) {
                        @Override
                        public void makeCallback() {
                            if (callback.getList() == null) {
                                showNetworkTestCompleteToast("No List with ID: " + listID);
                            } else {
                                showNetworkTestCompleteToast("Got List: " + callback.getList().getTitle());
                            }
                        }
                    };
                    callback.setDelegate(listDelegate);
                    network.makeGetListRequest(listID, callback);
                } else {
                    showNetworkTestCompleteToast("No Lists Available");
                }
            }
        });

        tests.add(new NetworkTest("Update List Test") {
            @Override
            public void executeTest() {
                if (lists.size() > 0) {
                    InventoryList list = lists.get(0);
                    list.setTitle(list.getTitle() + " - Update Test @" + new Date().getTime());
                    network.makeUpdateListRequest(list, new GenericCallback() {
                        @Override
                        public void callback() {
                            if (error != null) {
                                showNetworkTestCompleteToast(error.getMessage());
                                Log.e("ERROR-MESSAGE", error.getMessage());
                                Log.e("ERROR-DETAILS", error.getDetails());
                            } else {
                                showNetworkTestCompleteToast("Update List Complete");
                            }
                        }
                    });
                } else {
                    showNetworkTestCompleteToast("No Lists Available");
                }
            }
        });

        tests.add(new NetworkTest("Delete List Test") {
            @Override
            public void executeTest() {
                showNetworkTestCompleteToast("Unimplemented: Don't want to mess with data");
            }
        });

        // ITEM TESTS
        // ----------

        tests.add(new NetworkTest("Create New Item Test") {
            @Override
            public void executeTest() {
                InventoryItem item = new InventoryItem("", "Test Item", "Test Description", "https://pixabay.com/static/uploads/photo/2015/06/21/23/53/subtle-817155_960_720.jpg", "Test List ID", "", "", "", true, "", "");
                network.makeCreateItemRequest(item, new GenericCallback() {
                    @Override
                    public void callback() {
                        showNetworkTestCompleteToast("Item Created: " + data);
                    }
                });
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
