.class public Lcom/henryt/LifeCycle/MyActivity;
.super Landroid/app/Activity;
.source "MyActivity.java"


# static fields
.field public static Initialized:Z

.field public static STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

.field public static tag:Ljava/lang/String;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const-class v0, Lcom/henryt/LifeCycle/MyActivity;

    invoke-virtual {v0}, Ljava/lang/Class;->getSimpleName()Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/henryt/LifeCycle/MyActivity;->tag:Ljava/lang/String;

    const/4 v0, 0x0

    sput-boolean v0, Lcom/henryt/LifeCycle/MyActivity;->Initialized:Z

    const/4 v0, 0x0

    sput-object v0, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Landroid/app/Activity;-><init>()V

    return-void
.end method

.method public static test1()V
    .locals 10

    const/4 v4, 0x0

    invoke-static {v4}, Ljava/lang/System;->exit(I)V

    new-instance v2, Landroid/content/Intent;

    sget-object v4, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    const-class v5, Lcom/henryt/LifeCycle/MyActivity;

    invoke-direct {v2, v4, v5}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    const v1, 0x50466cd

    sget-object v4, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    const/high16 v5, 0x10000000

    invoke-static {v4, v1, v2, v5}, Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;

    move-result-object v0

    sget-object v4, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    const-string v5, "alarm"

    invoke-virtual {v4, v5}, Lcom/henryt/LifeCycle/MyActivity;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/app/AlarmManager;

    const/4 v4, 0x1

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    const-wide/16 v8, 0x64

    add-long/2addr v6, v8

    invoke-virtual {v3, v4, v6, v7, v0}, Landroid/app/AlarmManager;->set(IJLandroid/app/PendingIntent;)V

    return-void
.end method

.method public static test2()V
    .locals 3

    const/4 v1, 0x0

    invoke-static {v1}, Ljava/lang/System;->exit(I)V

    new-instance v0, Landroid/content/Intent;

    sget-object v1, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    invoke-virtual {v1}, Lcom/henryt/LifeCycle/MyActivity;->getBaseContext()Landroid/content/Context;

    move-result-object v1

    const-class v2, Lcom/henryt/LifeCycle/MyActivity;

    invoke-direct {v0, v1, v2}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    const/high16 v1, 0x4000000

    invoke-virtual {v0, v1}, Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;

    sget-object v1, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    invoke-virtual {v1, v0}, Lcom/henryt/LifeCycle/MyActivity;->startActivity(Landroid/content/Intent;)V

    return-void
.end method


# virtual methods
.method public onCreate(Landroid/os/Bundle;)V
    .locals 5

    const/16 v3, 0x80

    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    sput-object p0, Lcom/henryt/LifeCycle/MyActivity;->STATIC_REF:Lcom/henryt/LifeCycle/MyActivity;

    invoke-virtual {p0}, Lcom/henryt/LifeCycle/MyActivity;->getWindow()Landroid/view/Window;

    move-result-object v2

    invoke-virtual {v2, v3, v3}, Landroid/view/Window;->setFlags(II)V

    sget-object v2, Lcom/henryt/LifeCycle/MyActivity;->tag:Ljava/lang/String;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Initialized: "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    sget-boolean v4, Lcom/henryt/LifeCycle/MyActivity;->Initialized:Z

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v2, v3}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v2, 0x1

    sput-boolean v2, Lcom/henryt/LifeCycle/MyActivity;->Initialized:Z

    new-instance v0, Landroid/widget/Button;

    invoke-direct {v0, p0}, Landroid/widget/Button;-><init>(Landroid/content/Context;)V

    new-instance v1, Landroid/widget/RelativeLayout;

    invoke-direct {v1, p0}, Landroid/widget/RelativeLayout;-><init>(Landroid/content/Context;)V

    invoke-virtual {v1, v0}, Landroid/widget/RelativeLayout;->addView(Landroid/view/View;)V

    invoke-virtual {p0, v1}, Lcom/henryt/LifeCycle/MyActivity;->setContentView(Landroid/view/View;)V

    new-instance v2, Lcom/henryt/LifeCycle/MyActivity$1;

    invoke-direct {v2, p0}, Lcom/henryt/LifeCycle/MyActivity$1;-><init>(Lcom/henryt/LifeCycle/MyActivity;)V

    invoke-virtual {v0, v2}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    return-void
.end method
